package ru.various.sdp2partpost;

import ru.various.sdp2partpost.addressee.AddresseeFactory;
import ru.various.sdp2partpost.cases.SKOVSCaseFactory;
import ru.various.sdp2partpost.mvp.*;

import javax.swing.*;

public class MainApp {
    public static Model model;
    public static Presenter presenter;
    public static View view;

    public static void main(String[] args) {
        model = new Model(new AddresseeFactory(), new SKOVSCaseFactory());
        presenter = new Presenter();
        view = new View();
        presenter.setView(view);
        presenter.setModel(model);
        model.addObserver(presenter);
		view.addObserver(presenter);

        SwingUtilities.invokeLater(view);
    }
}
