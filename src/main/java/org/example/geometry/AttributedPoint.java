package org.example.geometry;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AttributedPoint {

    private List<Attribute> attributes = new ArrayList<>();

    public AttributedPoint(Attribute... attributes){
        this.attributes.addAll(List.of(attributes));
    }

    public Optional<Object> getAttributeValue(String name){
        return attributes.stream().filter(a -> a.name.equals(name))
                .findFirst()
                .map(op -> op.value);
    }

    public record Attribute(String name, Object value){}

}
