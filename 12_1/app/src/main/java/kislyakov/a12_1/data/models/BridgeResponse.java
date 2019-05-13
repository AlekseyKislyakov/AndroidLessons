
package kislyakov.a12_1.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BridgeResponse {

    @SerializedName("objects")
    @Expose
    private List<Bridge> bridges = null;

    public List<Bridge> getBridges() {
        return bridges;
    }

    public void setBridges(List<Bridge> bridges) {
        this.bridges = bridges;
    }

}
