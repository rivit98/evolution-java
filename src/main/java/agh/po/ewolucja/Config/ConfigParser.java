package agh.po.ewolucja.Config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class ConfigParser {

    public Config parse(String fname){
        Config c;
        try(Reader reader = new FileReader(fname)){
            c = this.parseCommon(reader);
            System.out.println("Using provided .json");
            return c;
        }catch (IOException e) {
            //what should i do?
        }

        return null;
    }

    public Config parseDefault(){
        Config c;

        try(Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/parameters.json"))){
            c = this.parseCommon(reader);
            System.out.println("Using default .json");
            return c;
        }catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Config parseCommon(Reader reader) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Config c = objectMapper.readValue(reader, Config.class);
        return c;
    }
}
