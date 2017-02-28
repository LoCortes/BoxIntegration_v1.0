package net.locortes.box.java.unirest.metadata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicenc.cortesolea on 31/01/2017.
 */
public class Metadata {
    private String templateKey;
    private String displayName;
    private String scope = "enterprise";
    private boolean hidden = false;
    private List<Field> fields;

    public Metadata(String templateKey, String displayName, boolean hidden) {
        this.templateKey = templateKey;
        this.displayName = displayName;
        this.hidden = hidden;
        fields = new ArrayList<Field>();
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getTemplateKey() {
        return templateKey;
    }

    public void setTemplateKey(String templateKey) {
        this.templateKey = templateKey;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public class Field{
        private String key;
        private String type;
        private String displayName;
        private boolean hidden;
        private List<Options> options;

        public Field(String key, String type, String displayName, boolean hidden, List<Options> options) {
            this.key = key;
            this.type = type;
            this.displayName = displayName;
            this.options = options;
            this.hidden = hidden;
        }

        public boolean isHidden() {
            return hidden;
        }

        public void setHidden(boolean hidden) {
            this.hidden = hidden;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public List<Options> getOptions() {
            return options;
        }

        public void setOptions(List<Options> options) {
            this.options = options;
        }

        @Override
        public String toString() {
            return "Field{" +
                    "key='" + key + '\'' +
                    ", type='" + type + '\'' +
                    ", displayName='" + displayName + '\'' +
                    ", options=" + options +
                    '}';
        }

        private class Options{
            private String key;

            public Options(String key) {
                this.key = key;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            @Override
            public String toString() {
                return key;
            }
        }
    }

    public void addField(Field field){
        fields.add(field);
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "templateKey='" + templateKey + '\'' +
                ", displayName='" + displayName + '\'' +
                ", scope='" + scope + '\'' +
                ", fields=" + fields +
                '}';
    }

}

