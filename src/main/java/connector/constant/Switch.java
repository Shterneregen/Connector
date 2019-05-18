/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connector.constant;

/**
 *
 * @author Yura
 */
public enum Switch {
    ON("on"),
    OFF("off");

    private String mode;

    private Switch(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

}
