package com.dan.api_example.model.image;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetImageRes {
    private byte[] data;

    public GetImageRes() {
    }

    public GetImageRes(byte[] data) {
        this.data = data;
    }
}