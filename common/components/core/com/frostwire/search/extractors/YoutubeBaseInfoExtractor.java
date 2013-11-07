package com.frostwire.search.extractors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeBaseInfoExtractor {

    public static interface Lambda1 {
        public Object run(Object obj);
    }

    public static interface LambdaN {
        public Object run(Object[] arr);
    }

    public static boolean isdigit(String str) {
        if (str.length() == 0) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isalpha(String str) {
        if (str.length() == 0) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isLetter(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static Object[] list(String str) {
        Object[] r = new Object[str.length()];
        for (int i = 0; i < str.length(); i++) {
            r[i] = str.charAt(i);
        }
        return r;
    }

    public static String join(Object[] arr) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : arr) {
            sb.append(obj.toString());
        }
        return sb.toString();
    }

    public static Integer len(Object obj) {
        if (obj instanceof Object[]) {
            return ((Object[]) obj).length;
        }

        if (obj instanceof String) {
            return ((String) obj).length();
        }

        throw new IllegalArgumentException("Not supported type");
    }

    public static Object reverse(Object obj) {
        if (obj instanceof Object[]) {
            List<Object> list = new ArrayList<Object>();
            list.addAll(Arrays.asList((Object[]) obj));
            Collections.reverse(list);
            return list.toArray();
        }

        if (obj instanceof String) {
            return new StringBuilder((String) obj).reverse().toString();
        }

        throw new IllegalArgumentException("Not supported type");
    }

    public static Object splice(Object obj, int fromIndex) {
        if (obj instanceof Object[]) {
            return Arrays.asList((Object[]) obj).subList(fromIndex, ((Object[]) obj).length).toArray();
        }

        if (obj instanceof String) {
            return ((String) obj).substring(fromIndex);
        }

        throw new IllegalArgumentException("Not supported type");
    }

    public final String jscode;
    public final Map<String, LambdaN> functions = new HashMap<String, LambdaN>();

    public YoutubeBaseInfoExtractor(String jscode) {
        this.jscode = jscode;
    }

    public Object interpret_statement(String stmt, final Map<String, Object> local_vars, final int allow_recursion) {
        if (allow_recursion < 0) {
            throw new ExtractorError("Recursion limit reached");
        }

        if (stmt.startsWith("var ")) {
            stmt = stmt.substring("var ".length());
        }

        final Matcher ass_m = Pattern.compile("^(?<out>[a-z]+)(\\[(?<index>.+?)\\])?=(?<expr>.*)$").matcher(stmt);
        Lambda1 assign;
        String expr;
        if (ass_m.find()) {
            if (ass_m.group("index") != null) {
                assign = new Lambda1() {
                    @Override
                    public Object run(Object val) {
                        Object lvar = local_vars.get(ass_m.group("out"));
                        Object idx = interpret_expression(ass_m.group("index"), local_vars, allow_recursion);
                        assert idx instanceof Integer;
                        ((Object[]) lvar)[(Integer) idx] = val;
                        return val;
                    }
                };
                expr = ass_m.group("expr");
            } else {
                assign = new Lambda1() {
                    @Override
                    public Object run(Object val) {
                        local_vars.put(ass_m.group("out"), val);
                        return val;
                    }
                };
                expr = ass_m.group("expr");
            }
        } else if (stmt.startsWith("return ")) {
            assign = new Lambda1() {
                @Override
                public Object run(Object v) {
                    return v;
                }
            };
            expr = stmt.substring("return ".length());
        } else {
            throw new ExtractorError(String.format("Cannot determine left side of statement in %s", stmt));
        }

        Object v = interpret_expression(expr, local_vars, allow_recursion);
        return assign.run(v);
    }

    public Object interpret_expression(String expr, Map<String, Object> local_vars, int allow_recursion) {
        if (isdigit(expr)) {
            return Integer.valueOf(expr);
        }

        if (isalpha(expr)) {
            return local_vars.get(expr);
        }

        Matcher m = Pattern.compile("^(?<in>[a-z]+)\\.(?<member>.*)$").matcher(expr);
        if (m.find()) {
            String member = m.group("member");
            Object val = local_vars.get(m.group("in"));
            if (member.equals("split(\"\")")) {
                return list((String) val);
            }
            if (member.equals("join(\"\")")) {
                return join((Object[]) val);
            }
            if (member.equals("length")) {
                return len(val);
            }
            if (member.equals("reverse()")) {
                return reverse(val);
            }
            Matcher slice_m = Pattern.compile("slice\\((?<idx>.*)\\)").matcher(member);
            if (slice_m.find()) {
                Object idx = interpret_expression(slice_m.group("idx"), local_vars, allow_recursion - 1);
                return splice(val, (Integer) idx);
            }
        }

        m = Pattern.compile("^(?<in>[a-z]+)\\[(?<idx>.+)\\]$").matcher(expr);
        if (m.find()) {
            Object val = local_vars.get(m.group("in"));
            Object idx = interpret_expression(m.group("idx"), local_vars, allow_recursion - 1);
            return ((Object[]) val)[(Integer) idx];
        }

        m = Pattern.compile("^(?<a>.+?)(?<op>[%])(?<b>.+?)$").matcher(expr);
        if (m.find()) {
            Object a = interpret_expression(m.group("a"), local_vars, allow_recursion);
            Object b = interpret_expression(m.group("b"), local_vars, allow_recursion);
            return (Integer) a % (Integer) b;
        }

        m = Pattern.compile("^(?<func>[a-zA-Z]+)\\((?<args>[a-z0-9,]+)\\)$").matcher(expr);
        if (m.find()) {
            String fname = m.group("func");
            if (!functions.containsKey(fname)) {
                functions.put(fname, extract_function(fname));
            }
            List<Object> argvals = new ArrayList<Object>();
            for (String v : m.group("args").split(",")) {
                if (isdigit(v)) {
                    argvals.add(Integer.valueOf(v));
                } else {
                    argvals.add(local_vars.get(v));
                }
            }
            return functions.get(fname).run(argvals.toArray());
        }
        throw new ExtractorError(String.format("Unsupported JS expression %s", expr));
    }

    public LambdaN extract_function(String funcname) {
        final Matcher func_m = Pattern.compile("function " + Pattern.quote(funcname) + "\\((?<args>[a-z,]+)\\)\\{(?<code>[^\\}]+)\\}").matcher(jscode);
        func_m.find();
        final String[] argnames = func_m.group("args").split(",");

        LambdaN resf = new LambdaN() {
            @Override
            public Object run(Object[] args) {
                Map<String, Object> local_vars = new HashMap<String, Object>();
                for (int i = 0; i < argnames.length; i++) {
                    local_vars.put(argnames[i], args[i]);
                }
                Object res = null;
                for (String stmt : func_m.group("code").split(";")) {
                    res = interpret_statement(stmt, local_vars, 20);
                    System.out.println(stmt);
                    if (res instanceof Object[]) {
                        System.out.println(join((Object[])res));
                    }
                }
                return res;
            }
        };

        return resf;
    }

    public Lambda1 parse_sig_js(String jscode) {
        Matcher m = Pattern.compile("signature=([a-zA-Z]+)").matcher(jscode);
        m.find();
        String funcname = m.group(1);

        final LambdaN initial_function = extract_function(funcname);

        return new Lambda1() {
            @Override
            public Object run(Object s) {
                return initial_function.run(new Object[] { s });
            }
        };
    }
}
