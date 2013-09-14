/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2013, FrostWire(R). All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.frostwire.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gubatron
 * @author aldenml
 *
 */
public final class ZipUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ZipUtils.class);

    public boolean unzip(String zipFile, File outputDir) {
        return unzip(zipFile, outputDir, null);
    }

    public boolean unzip(String zipFile, File outputDir, ZipListener listener) {
        boolean result = false;

        try {

            FileUtils.deleteDirectory(outputDir);

            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            try {
                unzipEntries(outputDir, zis, System.currentTimeMillis(), listener);
            } finally {
                zis.close();
            }

            result = true;

        } catch (IOException e) {
            LOG.error("Unable to uncompress " + zipFile + " to " + outputDir, e);
            result = false;
        }

        return result;
    }

    private void unzipEntries(File folder, ZipInputStream zis, long time, ZipListener listener) throws IOException, FileNotFoundException {
        ZipEntry ze = null;

        while ((ze = zis.getNextEntry()) != null) {

            String fileName = ze.getName();
            File newFile = new File(folder, fileName);

            LOG.debug("unzip: " + newFile.getAbsoluteFile());

            if (ze.isDirectory()) {
                if (!newFile.mkdirs()) {
                    break;
                }
                continue;
            }

            if (listener != null) {
                listener.onUnzipping(newFile);
            }

            FileOutputStream fos = new FileOutputStream(newFile);

            try {
                int n;
                byte[] buffer = new byte[1024];
                while ((n = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, n);
                }
            } finally {
                fos.close();
                zis.closeEntry();
            }

            newFile.setLastModified(time);
        }
    }

    public static interface ZipListener {
        public void onUnzipping(File file);
    }
}
