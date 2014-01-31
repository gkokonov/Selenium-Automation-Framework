/* ***********************************************************************
 * Copyright 2013 ScoreTwice, All rights reserved. ScoreTwice Confidential
 * ***********************************************************************
 * Date: 20.07.2013
 * Author: G.Kokonov
 * ***********************************************************************/

package com.gk.webdriver.utils;

import java.io.InputStream;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Execute SSh command on a remote linux machine
 * 
 */
public class SSHExec {
    private static final Logger LOGGER = Logger.getLogger(SSHExec.class);

    public static Boolean executeSshCommand(String sshHost, String sshPassword,
            String sshCommand) {
        Boolean result = false;
        try {
            JSch.setConfig("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();

            String host = sshHost;
            String user = host.substring(0, host.indexOf('@'));
            host = host.substring(host.indexOf('@') + 1);

            Session session = jsch.getSession(user, host, 22);
            session.setPassword(sshPassword);
            session.connect();

            String command = sshCommand;

            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            // X Forwarding
            // channel.setXForwarding(true);

            //channel.setInputStream(System.in);
            channel.setInputStream(null);

            //channel.setOutputStream(System.out);

            //FileOutputStream fos=new FileOutputStream("/tmp/stderr");
            //((ChannelExec)channel).setErrStream(fos);
            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (channel.getExitStatus() == 0) {
                        result = true;
                    }
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return result;
    }
}
