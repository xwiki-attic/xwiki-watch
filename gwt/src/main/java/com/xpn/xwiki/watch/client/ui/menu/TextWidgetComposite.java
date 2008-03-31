package com.xpn.xwiki.watch.client.ui.menu;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class TextWidgetComposite extends Composite
{
    protected Widget mainWidget;
    protected FlowPanel widgetsPanel;
    public TextWidgetComposite(Widget mainWidget)
    {
        this.mainWidget = mainWidget;
        this.widgetsPanel = new FlowPanel();
        
        Panel compositePanel = new FlowPanel();
        compositePanel.add(this.mainWidget);
        compositePanel.add(this.widgetsPanel);
        //every composite has to call initWidget in their constructors
        initWidget(compositePanel);
    }
    
    public void add(TextWidgetComposite cText) {
        this.widgetsPanel.add(cText);
    }
    
    public void remove(TextWidgetComposite cText) {
        this.widgetsPanel.remove(cText);
    }
    public List getWidgets() {
        ArrayList widgets = new ArrayList();
        for (int i = 0; i < this.widgetsPanel.getWidgetCount(); i++) {
            widgets.add(this.widgetsPanel.getWidget(i));
        }
        return widgets;
    }
}
