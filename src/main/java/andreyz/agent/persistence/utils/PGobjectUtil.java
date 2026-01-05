package andreyz.agent.persistence.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;

public class PGobjectUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    public static PGobject toJsonb(Object value) {
        try {
            PGobject obj = new PGobject();
            obj.setType("jsonb");
            obj.setValue(OBJECT_MAPPER.writeValueAsString(value));
            return obj;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to convert to jsonb", e);
        }
    }


}