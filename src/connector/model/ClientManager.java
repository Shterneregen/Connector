/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connector.model;

import connector.utils.Utils;
import java.util.List;

/**
 *
 * @author Yura
 */
public class ClientManager {
    private Client client;
    // Массив локальных IP адресов
    private List<String> listAddr = Utils.getMyLocalIP();
    
}
