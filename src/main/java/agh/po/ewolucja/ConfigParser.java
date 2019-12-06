package agh.po.ewolucja;

import java.io.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigParser {

    private boolean usingDefaults = false;

    public Config parse(String fname){
        Config c;
        try(Reader reader = new FileReader(fname)){
            c = this.parseCommon(reader);
            System.out.println("Using provided .json");
        }catch (IOException e) {
            //what should i do?
            usingDefaults = true;
        }

        if(usingDefaults){
            try(Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/parameters.json"))){
                c = this.parseCommon(reader);
                System.out.println("Using default .json");
                return c;
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private Config parseCommon(Reader reader) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Config c = objectMapper.readValue(reader, Config.class);
        return c;
    }
}
