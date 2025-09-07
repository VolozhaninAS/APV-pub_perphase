package org.example.iec61850.datatypes.common;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Timestamp extends Data {
    private long value;
}
