import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        
        EmpowermentResponse response = new EmpowermentResponse();
        response.setEmpowermentRestrictionDetails(new EmpowermentRestrictionDetails()); // Empty object

        String json = objectMapper.writeValueAsString(response);
        System.out.println(json); // Should print "{}" (without empty object)
    }
}
