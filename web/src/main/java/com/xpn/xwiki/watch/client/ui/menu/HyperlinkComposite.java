package com.xpn.xwiki.watch.client.ui.menu;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Panel;

public class HyperlinkComposite extends Composite
{
    protected Hyperlink mainWidget;
    protected FlowPanel hyperlinksPanel;
    public HyperlinkComposite(Hyperlink mainWidget)
    {
        this.mainWidget = mainWidget;
        this.hyperlinksPanel = new FlowPanel();
        
        Panel compositePanel = new FlowPanel();
        compositePanel.add(this.mainWidget);
        compositePanel.add(this.hyperlinksPanel);
        //every composite has to call initWidget in their constructors
        initWidget(compositePanel);
    }
    
    public void add(HyperlinkComposite cHyperlink) {
        this.hyperlinksPanel.add(cHyperlink);
    }
    
    public void remove(HyperlinkComposite cHyperlink) {
        this.hyperlinksPanel.remove(cHyperlink);
    }
}
