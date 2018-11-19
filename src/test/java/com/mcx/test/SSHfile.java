package com.mcx.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Ext-MayukhG on 9/5/2018.
 */
public class SSHfile {
    public void runShellShript() throws IOException {
        try{
            String myKey="/path/pemfileName";
            Runtime runtime = Runtime.getRuntime();
            String commande = "ssh -i "+myKey+" userName@IP"+" /path/shellScripFile.sh";
            Process p = runtime.exec(commande);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
            p.waitFor();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
