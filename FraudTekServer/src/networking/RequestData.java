package networking;

/**
 * Created by Bailey on 3/30/2018.
 */
public class RequestData {
    private int intValue;
    private String stringValue;

    public RequestData(int value, String strVal){
        intValue = value;
        stringValue = strVal;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    // standard getters and setters
}
