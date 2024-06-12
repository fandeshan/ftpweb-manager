package com.fds.ftpweb;

/**
 * @author fandeshan
 * @description //TODO 写点注释吧
 * @date 2024/6/11
 */

public class FileInfo {
    private String name;
    private boolean isDirectory;

    public FileInfo(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }

    public String getName() {
        return name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }
}
