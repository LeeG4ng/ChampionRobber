package io.github.leeg4ng.robber.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class ClientUtils {

    public static ClientInfo currentClientInfo;
    
    public static ClientInfo getClientInfo() throws ClientNotFoundException {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("wmic PROCESS WHERE name='LeagueClientUx.exe' GET commandline");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String res = bufferedReader.lines().collect(Collectors.joining("\n"));
            log.info(res);

            Pattern portPattern = Pattern.compile("\"--app-port=([0-9]*)\"");
            Matcher portMatcher = portPattern.matcher(res);
            Pattern tokenPattern = Pattern.compile("\"--remoting-auth-token=([\\w-]*)\"");
            Matcher tokenMatcher = tokenPattern.matcher(res);
            if (portMatcher.find() && tokenMatcher.find()) {
                String port = portMatcher.group(1);
                String token = tokenMatcher.group(1);
                log.info("port:{} token:{}", port, token);
                return ClientInfo.builder().port(port).token(token).build();
            } else {
                throw new ClientNotFoundException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Data
    @Builder
    public static class ClientInfo {
        
        private String port;
        
        private String token;
    }

    public static class ClientNotFoundException extends Exception {

    }
}
