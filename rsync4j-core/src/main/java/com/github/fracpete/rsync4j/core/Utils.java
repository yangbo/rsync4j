/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Utils.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.rsync4j.core;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

import static com.sun.jna.platform.win32.WinNT.PROCESS_ALL_ACCESS;


/**
 * Various helper methods.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class Utils {

    /**
     * Flattens the list.
     *
     * @param parts the list to flatten
     * @param sep   the separator to use
     * @return the flattened array
     */
    public static String flatten(List parts, String sep) {
        Object[] array;
        int i;

        array = new Object[parts.size()];
        for (i = 0; i < parts.size(); i++)
            array[i] = parts.get(i);

        return flatten(array, sep);
    }

    /**
     * Flattens the array.
     *
     * @param parts the array to flatten
     * @param sep   the separator to use
     * @return the flattened array
     */
    public static String flatten(Object[] parts, String sep) {
        StringBuilder result;
        int i;

        result = new StringBuilder();
        for (i = 0; i < parts.length; i++) {
            if (i > 0)
                result.append(sep);
            result.append(parts[i].toString());
        }

        return result.toString();
    }

    /**
     * Test if the port is listening.
     *
     * @param port to test
     * @return true if the port is listening
     * @author bob
     */
    public static boolean isListening(int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(port));
            return true;
        } catch (Exception ex) {
            // 端口不可用
            return false;
        }
    }

    /**
     * Check if the specified process is running.
     *
     * @param exeName The process name, usually just exe name such as 'rsync.exe'.
     * @return true if the process is running.
     *
     * @author bob
     */
    public static boolean isProcessRunningWin(String exeName) {
        return findProcessWin(exeName).isPresent();
    }

    /**
     * Find the process of specified name.
     * @param exeName The process name, usually just the exe name.
     * @return Optional object of PROCESSENTRY32 structure.
     */
    public static Optional<Tlhelp32.PROCESSENTRY32> findProcessWin(String exeName) {
        Kernel32 kernel32 = Kernel32.INSTANCE;
        Tlhelp32.PROCESSENTRY32 pe32 = new Tlhelp32.PROCESSENTRY32();
        // 设置结构体大小
        pe32.dwSize = new WinDef.DWORD(pe32.size());

        // 创建进程快照
        WinNT.HANDLE snapshot = kernel32.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0));
        if (snapshot == WinNT.INVALID_HANDLE_VALUE) {
            throw new RuntimeException("Create process snapshot failed!");
        }

        try {
            // 遍历进程
            if (kernel32.Process32First(snapshot, pe32)) {
                do {
                    // 比较进程名，忽略大小写
                    int len = 0;
                    for (int i = 0; i < pe32.szExeFile.length; i++) {
                        if (pe32.szExeFile[i] == 0) {
                            break;
                        }
                        len++;
                    }
                    if (new String(pe32.szExeFile, 0, len).equalsIgnoreCase(exeName)) {
                        // 找到匹配的进程
                        return Optional.of(pe32);
                    }
                } while (kernel32.Process32Next(snapshot, pe32));
            }
        } finally {
            // 关闭进程快照
            kernel32.CloseHandle(snapshot);
        }
        return Optional.empty();
    }

    /**
     * Terminate the process.
     * @param exeName the process name, usually just exe name.
     * @return true if TerminateProcess() return true or there is no such process.
     */
    public static boolean killProcessWin(String exeName) {
        Optional<Tlhelp32.PROCESSENTRY32> pe32 = findProcessWin(exeName);
        if (!pe32.isPresent()) {
            return true;
        }
        int pid = pe32.get().th32ProcessID.intValue();
        Kernel32 kernel32 = Kernel32.INSTANCE;
        WinNT.HANDLE hProcess = kernel32.OpenProcess(PROCESS_ALL_ACCESS, false, pid);
        boolean ret = kernel32.TerminateProcess(hProcess, 0);
        kernel32.CloseHandle(hProcess);
        return ret;
    }
}
