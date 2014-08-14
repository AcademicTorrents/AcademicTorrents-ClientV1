/*
 * File    : TOTorrentFileImpl.java
 * Created : 5 Oct. 2003
 * By      : Parg 
 * 
 * Azureus - a Java Bittorrent client
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details ( see the LICENSE file ).
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.frostwire.torrent;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

final class TOTorrentFileImpl implements TOTorrentFile {
    private final TOTorrent torrent;
    private final long file_length;
    private final byte[][] path_components;
    private final byte[][] path_components_utf8;

    private final int first_piece_number;
    private final int last_piece_number;

    private final Map<String, Object> additional_properties = new HashMap<String, Object>(1);

    private final boolean is_utf8;

    protected TOTorrentFileImpl(TOTorrent _torrent, long _torrent_offset, long _len, String _path)

    throws TOTorrentException {
        torrent = _torrent;
        file_length = _len;

        first_piece_number = (int) (_torrent_offset / torrent.getPieceLength());
        last_piece_number = (int) ((_torrent_offset + file_length - 1) / torrent.getPieceLength());

        is_utf8 = true;

        try {

            Vector<byte[]> temp = new Vector<byte[]>();

            int pos = 0;

            while (true) {

                int p1 = _path.indexOf(File.separator, pos);

                if (p1 == -1) {

                    temp.add(_path.substring(pos).getBytes(Constants.DEFAULT_ENCODING));

                    break;
                }

                temp.add(_path.substring(pos, p1).getBytes(Constants.DEFAULT_ENCODING));

                pos = p1 + 1;
            }

            path_components = new byte[temp.size()][];

            temp.copyInto(path_components);

            path_components_utf8 = new byte[temp.size()][];

            temp.copyInto(path_components_utf8);

            checkComponents();

        } catch (UnsupportedEncodingException e) {

            throw (new TOTorrentException("Unsupported encoding for '" + _path + "'", TOTorrentException.RT_UNSUPPORTED_ENCODING));
        }
    }

    protected TOTorrentFileImpl(TOTorrent _torrent, long _torrent_offset, long _len, byte[][] _path_components)

    throws TOTorrentException {
        torrent = _torrent;
        file_length = _len;
        path_components = _path_components;
        path_components_utf8 = null;

        first_piece_number = (int) (_torrent_offset / torrent.getPieceLength());
        last_piece_number = (int) ((_torrent_offset + file_length - 1) / torrent.getPieceLength());

        is_utf8 = false;

        checkComponents();
    }

    protected TOTorrentFileImpl(TOTorrent _torrent, long _torrent_offset, long _len, byte[][] _path_components, byte[][] _path_components_utf8)

    throws TOTorrentException {
        torrent = _torrent;
        file_length = _len;
        path_components = _path_components;
        path_components_utf8 = _path_components_utf8;

        first_piece_number = (int) (_torrent_offset / torrent.getPieceLength());
        last_piece_number = (int) ((_torrent_offset + file_length - 1) / torrent.getPieceLength());

        is_utf8 = false;

        checkComponents();
    }

    protected void checkComponents()

    throws TOTorrentException {
        byte[][][] to_do = { path_components, path_components_utf8 };

        for (byte[][] pc : to_do) {

            if (pc == null) {
                continue;
            }

            for (int i = 0; i < pc.length; i++) {

                byte[] comp = pc[i];
                if (comp.length == 2 && comp[0] == (byte) '.' && comp[1] == (byte) '.')
                    throw (new TOTorrentException("Torrent file contains illegal '..' component", TOTorrentException.RT_DECODE_FAILS));

                // intern directories as they're likely to repeat
                //				if(i < (pc.length - 1)){
                //					//pc[i] = StringInterner.internBytes(pc[i]);
                //				}
            }
        }
    }

    public TOTorrent getTorrent() {
        return (torrent);
    }

    public long getLength() {
        return (file_length);
    }

    public byte[][] getPathComponentsBasic() {
        return (path_components);
    }

    public byte[][] getPathComponents() {
        return path_components_utf8 == null ? path_components : path_components_utf8;
    }

    public byte[][] getPathComponentsUTF8() {
        return (path_components_utf8);
    }

    protected boolean isUTF8() {
        return (is_utf8);
    }

    protected void setAdditionalProperty(String name, Object value) {
        additional_properties.put(name, value);
    }

    protected Map<String, Object> getAdditionalProperties() {
        return (additional_properties);
    }

    public int getFirstPieceNumber() {
        return (first_piece_number);
    }

    public int getLastPieceNumber() {
        return (last_piece_number);
    }

    public int getNumberOfPieces() {
        return (getLastPieceNumber() - getFirstPieceNumber() + 1);
    }

    public String getRelativePath() {
        if (torrent == null) {
            return "";
        }
        String sRelativePath = "";

        byte[][] pathComponentsUTF8 = getPathComponentsUTF8();
        if (pathComponentsUTF8 != null) {
            for (int j = 0; j < pathComponentsUTF8.length; j++) {

                try {
                    String comp;
                    try {
                        comp = new String(pathComponentsUTF8[j], "utf8");
                    } catch (UnsupportedEncodingException e) {
                        System.out.println("file - unsupported encoding!!!!");
                        comp = "UnsupportedEncoding";
                    }

                    comp = convertOSSpecificChars(comp, j != pathComponentsUTF8.length - 1);

                    sRelativePath += (j == 0 ? "" : File.separator) + comp;
                } catch (Exception ex) {
                    Debug.out(ex);
                }

            }
            return sRelativePath;
        }

        LocaleUtilDecoder decoder = null;
        try {
            decoder = LocaleTorrentUtil.getTorrentEncodingIfAvailable(torrent);
            if (decoder == null) {
                LocaleUtil localeUtil = LocaleUtil.getSingleton();
                decoder = localeUtil.getSystemDecoder();
            }
        } catch (Exception e) {
            // Do Nothing
        }

        if (decoder != null) {
            byte[][] components = getPathComponents();
            for (int j = 0; j < components.length; j++) {

                try {
                    String comp;
                    try {
                        comp = decoder.decodeString(components[j]);
                    } catch (UnsupportedEncodingException e) {
                        System.out.println("file - unsupported encoding!!!!");
                        try {
                            comp = new String(components[j]);
                        } catch (Exception e2) {
                            comp = "UnsupportedEncoding";
                        }
                    }

                    comp = convertOSSpecificChars(comp, j != components.length - 1);

                    sRelativePath += (j == 0 ? "" : File.separator) + comp;
                } catch (Exception ex) {
                    Debug.out(ex);
                }

            }

        }
        return sRelativePath;
    }

    /**
     * @return
     *
     * @since 4.1.0.5
     */
    public Map<String, Object> serializeToMap() {
        Map<String, Object> file_map = new HashMap<String, Object>();

        file_map.put(TOTorrentImpl.TK_LENGTH, new Long(getLength()));

        List<byte[]> path = new ArrayList<byte[]>();

        file_map.put(TOTorrentImpl.TK_PATH, path);

        byte[][] path_comps = getPathComponentsBasic();

        if (path_comps != null) {
            for (int j = 0; j < path_comps.length; j++) {

                path.add(path_comps[j]);
            }
        }

        if (path_comps != null && isUTF8()) {

            List<byte[]> utf8_path = new ArrayList<byte[]>();

            file_map.put(TOTorrentImpl.TK_PATH_UTF8, utf8_path);

            for (int j = 0; j < path_comps.length; j++) {

                utf8_path.add(path_comps[j]);
            }
        } else {

            byte[][] utf8_path_comps = getPathComponentsUTF8();

            if (utf8_path_comps != null) {
                List<byte[]> utf8_path = new ArrayList<byte[]>();

                file_map.put(TOTorrentImpl.TK_PATH_UTF8, utf8_path);

                for (int j = 0; j < utf8_path_comps.length; j++) {

                    utf8_path.add(utf8_path_comps[j]);
                }
            }
        }

        Map<String, Object> file_additional_properties = getAdditionalProperties();

        Iterator<String> prop_it = file_additional_properties.keySet().iterator();

        while (prop_it.hasNext()) {

            String key = (String) prop_it.next();

            file_map.put(key, file_additional_properties.get(key));
        }

        return file_map;
    }

    private static String convertOSSpecificChars(String file_name_in, boolean is_folder) {
        // this rule originally from DiskManager

        char[] chars = file_name_in.toCharArray();

        for (int i = 0; i < chars.length; i++) {

            if (chars[i] == '"') {

                chars[i] = '\'';
            }
        }

        if (!Constants.isOSX) {

            if (Constants.isWindows) {

                //  this rule originally from DiskManager

                // The definitive list of characters permitted for Windows is defined here:
                // http://support.microsoft.com/kb/q120138/
                String not_allowed = "\\/:?*<>|";
                for (int i = 0; i < chars.length; i++) {
                    if (not_allowed.indexOf(chars[i]) != -1) {
                        chars[i] = '_';
                    }
                }

                // windows doesn't like trailing dots and whitespaces in folders, replace them

                if (is_folder) {

                    for (int i = chars.length - 1; i >= 0 && (chars[i] == '.' || chars[i] == ' '); chars[i] = '_', i--)
                        ;
                }
            }

            // '/' is valid in mac file names, replace with space
            // so it seems are cr/lf

            for (int i = 0; i < chars.length; i++) {

                char c = chars[i];

                if (c == '/' || c == '\r' || c == '\n') {

                    chars[i] = ' ';
                }
            }
        }

        String file_name_out = new String(chars);

        try {

            // mac file names can end in space - fix this up by getting
            // the canonical form which removes this on Windows

            // however, for soem reason getCanonicalFile can generate high CPU usage on some user's systems
            // in  java.io.Win32FileSystem.canonicalize
            // so changing this to only be used on non-windows

            if (Constants.isWindows) {

                while (file_name_out.endsWith(" ")) {

                    file_name_out = file_name_out.substring(0, file_name_out.length() - 1);
                }

            } else {

                String str = new File(file_name_out).getCanonicalFile().toString();

                int p = str.lastIndexOf(File.separator);

                file_name_out = str.substring(p + 1);
            }

        } catch (Throwable e) {
            // ho hum, carry on, it'll fail later
            //e.printStackTrace();
        }

        //System.out.println( "convertOSSpecificChars: " + file_name_in + " ->" + file_name_out );

        return (file_name_out);
    }
}
