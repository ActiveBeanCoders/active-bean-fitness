package com.activebeancoders.fitness.common;

/**
 * @author Dan Barrese
 */
public class RestApiArgument {

    public String type;
    public String dataType;
    public String name;
    public String required;

    public RestApiArgument(String[] args) {
        type = args[0];
        dataType = args[1];
        name = args[2];
        required = args[3];
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RestApiArgument{");
        sb.append("type='").append(type).append('\'');
        sb.append(", dataType='").append(dataType).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", required='").append(required).append('\'');
        sb.append('}');
        return sb.toString();
    }

}

