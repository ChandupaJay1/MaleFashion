package dto;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

public class Response_DTO implements Serializable {

    @Expose
    private boolean success;
    
    @Expose
    private Object content;

    public Response_DTO() {
    }

    public Response_DTO(boolean success, Object content) {
        this.success = success;
        this.content = content;
    }

    /**
     * @return the status
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success the status to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return the content
     */
    public Object getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(Object content) {
        this.content = content;
    }

}
