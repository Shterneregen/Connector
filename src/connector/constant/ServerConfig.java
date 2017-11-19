/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connector.constant;

/**
 * Сервер запущен из клиента или один
 *
 * @author Yura
 */
public enum ServerConfig {
    /**
     * Сервер запущен из клиента
     */
    SERVER_FROM_CLIENT,
    /**
     * Сервер запущен без клиента
     */
    ONLY_SERVER;
}
