/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calendar;

/**
 *
 * @author LSJ
 */

import java.time.LocalDateTime;

public class Event {

    private String title;
    private LocalDateTime startDateTime;

    public Event(String title, LocalDateTime startDateTime) {
        this.title = title;
        this.startDateTime = startDateTime;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }
}