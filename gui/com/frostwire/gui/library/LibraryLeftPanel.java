package com.frostwire.gui.library;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;


public class LibraryLeftPanel extends JPanel {

    private static final long serialVersionUID = -2924157073406477820L;

    public static final int MIN_WIDTH = 155;
    public static final int MAX_WIDTH = 300;

    private final LibraryExplorer libraryExplorer;
    private final LibraryPlaylists libraryPlaylists;
    private final LibraryCoverArt libraryCoverArt;

    public LibraryLeftPanel(LibraryExplorer libraryExplorer, LibraryPlaylists libraryPlaylists, LibraryCoverArt libraryCoverArt) {
        this.libraryExplorer = libraryExplorer;
        this.libraryPlaylists = libraryPlaylists;
        this.libraryCoverArt = libraryCoverArt;

        setupUI();
    }

    protected void setupUI() {
        setLayout(null);

        add(libraryExplorer);
        add(libraryPlaylists);
        add(libraryCoverArt);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                layoutComponents();
            }
        });
    }

    protected void layoutComponents() {
        Dimension size = getSize();

        // layout cover art
        int coverArtWidth = size.width > MAX_WIDTH ? MAX_WIDTH : size.width;
        libraryCoverArt.setLocation(0, coverArtWidth < size.height ? size.height - coverArtWidth : 0);
        libraryCoverArt.setSize(coverArtWidth, coverArtWidth);

        // layout files and playlists
        int heightMinusCover = size.height - coverArtWidth;
        int fileRowHeight = libraryExplorer.getRowDimension().height;
        int playlistRowHeight = libraryPlaylists.getRowDimension().height;

        if (3 * (fileRowHeight + playlistRowHeight) > heightMinusCover) {
            // too small, split even
            libraryExplorer.setLocation(0, 0);
            libraryExplorer.setSize(size.width, heightMinusCover / 2);
            libraryPlaylists.setLocation(0, heightMinusCover / 2);
            libraryPlaylists.setSize(size.width, heightMinusCover - heightMinusCover / 2);
        } else if ((13) * fileRowHeight + 3 * playlistRowHeight > heightMinusCover) {
            // too small for complete display of files
            int libraryFilesHeight = heightMinusCover - 3 * playlistRowHeight;
            libraryExplorer.setLocation(0, 0);
            libraryExplorer.setSize(size.width, libraryFilesHeight);
            libraryPlaylists.setLocation(0, libraryFilesHeight);
            libraryPlaylists.setSize(size.width, heightMinusCover - libraryFilesHeight);
        } else {
            // complete display of files
            int libraryFilesHeight = (13) * fileRowHeight;
            libraryExplorer.setLocation(0, 0);
            libraryExplorer.setSize(size.width, libraryFilesHeight);
            libraryPlaylists.setLocation(0, libraryFilesHeight);
            libraryPlaylists.setSize(size.width, heightMinusCover - libraryFilesHeight);
        }
    }

}
