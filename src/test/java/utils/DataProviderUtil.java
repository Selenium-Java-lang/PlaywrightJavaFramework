package utils;

import org.testng.annotations.DataProvider;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class DataProviderUtil {
    @DataProvider
    public Object[][] jsonDataProvider(){
        String filePath = "src/test/Resources/eventBookingData.json";
        File jsonContent = new File(filePath);
        ObjectMapper mapper = new ObjectMapper();
        List<HashMap<String,String>> data = mapper.readValue(jsonContent,
                new TypeReference<List<HashMap<String,String>>>(){});
        Object[][] dataArray = new Object[data.size()][1];
        for(int i= 0; i<data.size();i++){
            dataArray[i] = new Object[]{data.get(i)};
        }
        return dataArray;

    }
}
