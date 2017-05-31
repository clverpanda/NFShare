package com.clverpanda.nfshare.model.communicate.receive;

/**
 * Created by clverpanda on 2017/5/26 0026.
 * It's the file for NFShare.
 */

public class StartShareRec
{
    private String status;
    private int pin;
    private int id;

    public StartShareRec() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
