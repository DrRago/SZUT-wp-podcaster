/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package backend.fileTransfer;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.*;

/***
 * This is an example program demonstrating how to use the FTPSClient class.
 * This program connects to an FTP server and retrieves the specified
 * file.  If the -s flag is used, it stores the local file at the FTP server.
 * Just so you can see what's happening, all reply strings are printed.
 * If the -b flag is used, a binary transfer is assumed (default is ASCII).
 * <p>
 * Usage: ftp [-s] [-b] <hostname> <username> <password> <remote file> <local file>
 * <p>
 ***/
public class FTPSUploader implements Uploader {

    private FTPSClient ftps;
    private String username;
    private String password;
    private String remote;

    public FTPSUploader() {
        String server;
        String protocol = "SSL";    // SSL/TLS

        server = "localhost";
        username = "test";
        password = "1234";
        remote = "";

        ftps = new FTPSClient(protocol);

        ftps.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        try {
            int reply;

            ftps.connect(server, 21);

            // After connection attempt, you should check the reply code to verify success.
            reply = ftps.getReplyCode();

            System.out.println(reply);

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftps.disconnect();
            }
        } catch (IOException e) {
            if (ftps.isConnected()) {
                try {
                    ftps.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void uploadFile(String filePath) {
        try {
            ftps.setBufferSize(1000);

            if (!ftps.login(username, password)) {
                ftps.logout();
            }


            // Use passive mode as default because most of us are
            // behind firewalls these days.
            // ftps.enterLocalPassiveMode();

            InputStream input;
            input = new FileInputStream(new File(filePath));
            ftps.storeFile(remote, input);

            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            ftps.logout();
            ftps.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}