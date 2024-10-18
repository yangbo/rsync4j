package com.github.fracpete.rsync4j.core.test;

import com.github.fracpete.rsync4j.core.Utils;
import com.sun.jna.platform.win32.Tlhelp32;

import java.util.Optional;

/**
 * @author yangbo
 */
class UtilsTest {

    @org.junit.jupiter.api.Test
    void isListening() {
        int port = 12000;
        boolean listening = Utils.isListening(port);
        if (listening) {
            System.out.println("rsync is listening port: " + port);
        } else {
            System.out.println("rsync is not running!");
        }
    }

    @org.junit.jupiter.api.Test
    void isProcessRunningWin() {
        boolean running = Utils.isProcessRunningWin("rsync.exe");
        if (running) {
            System.out.println("rsync is running!");
        } else {
            System.out.println("rsync is not running!");
        }
    }

    @org.junit.jupiter.api.Test
    void findProcessWin() {
        Optional<Tlhelp32.PROCESSENTRY32> pe32 = Utils.findProcessWin("rsync.exe");
        if (pe32.isPresent()) {
            System.out.println("rsync pid: " + pe32.get().th32ProcessID);
        } else {
            System.out.println("rsync is not running!");
        }
    }

    @org.junit.jupiter.api.Test
    void killProcessWin() {
        Optional<Tlhelp32.PROCESSENTRY32> pe32 = Utils.findProcessWin("rsync.exe");
        if (pe32.isPresent()) {
            System.out.println("rsync pid: " + pe32.get().th32ProcessID);
        } else {
            System.out.println("rsync is not running!");
        }
        boolean ret = Utils.killProcessWin("rsync.exe");
        System.out.println("kill rsync.exe return: " + ret);
    }
}