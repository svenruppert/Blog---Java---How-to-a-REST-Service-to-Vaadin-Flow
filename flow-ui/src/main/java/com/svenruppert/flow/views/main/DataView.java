package com.svenruppert.flow.views.main;

import com.svenruppert.flow.MainLayout;
import com.svenruppert.flow.client.ExternalApiService;
import com.svenruppert.restdemo.DataModel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.io.IOException;

/**
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message in a notification.
 */
@Route(value = "", layout = MainLayout.class)
public class DataView
    extends VerticalLayout {

  public DataView() {
    ExternalApiService apiService = new ExternalApiService("http://localhost:9090");
    try {
      DataModel.DataObject data = apiService.fetchData();
      showData(data);
    } catch (IOException | InterruptedException e) {
      showError(e.getMessage());
    }
  }

  private void showData(DataModel.DataObject data) {
    add(new Span("ID: " + data.id()));
    add(new Span("Wert: " + data.value()));
  }

  private void showError(String message) {
    Notification.show("Fehler beim Laden der Daten: " + message, 5000, Notification.Position.MIDDLE);
  }
}