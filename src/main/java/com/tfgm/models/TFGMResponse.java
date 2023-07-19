package com.tfgm.models;

import org.json.JSONObject;

public class TFGMResponse {
  private JSONObject response;
  private Long timestamp;

  public TFGMResponse(Long timestamp,JSONObject response) {
    this.response = response;
    this.timestamp = timestamp;
  }

  public JSONObject getResponse() {
    return response;
  }

  public void setResponse(JSONObject response) {
    this.response = response;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
}
