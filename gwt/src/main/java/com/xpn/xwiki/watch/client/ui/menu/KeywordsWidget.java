package com.xpn.xwiki.watch.client.ui.menu;

import com.xpn.xwiki.gwt.api.client.app.XWikiAsyncCallback;
import com.xpn.xwiki.gwt.api.client.dialog.Dialog;
import com.xpn.xwiki.watch.client.ui.WatchWidget;
import com.xpn.xwiki.watch.client.ui.dialog.GroupDialog;
import com.xpn.xwiki.watch.client.ui.dialog.KeywordDialog;
import com.xpn.xwiki.watch.client.Watch;
import com.xpn.xwiki.watch.client.data.Group;
import com.xpn.xwiki.watch.client.data.Keyword;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.*;

/**
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * <p/>
 * This is free software;you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation;either version2.1of
 * the License,or(at your option)any later version.
 * <p/>
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY;without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the GNU
 * Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software;if not,write to the Free
 * Software Foundation,Inc.,51 Franklin St,Fifth Floor,Boston,MA
 * 02110-1301 USA,or see the FSF site:http://www.fsf.org.
 *
 * @author ldubost
 */

public class KeywordsWidget extends WatchWidget {
    private Map keywordsLink = new HashMap();
    private FlowPanel keywordsPanel = new FlowPanel();

    public KeywordsWidget() {
        super();
    }

    public String getName() {
        return "keywords";
    }

    public KeywordsWidget(Watch watch) {
        super(watch);
        panel = new FlowPanel();
        panel.add(getTitlePanel());
        panel.add(keywordsPanel);
        initWidget(panel);
        init();
    }

    private Widget getTitlePanel() {
        FlowPanel p = new FlowPanel();
        p.setStyleName(watch.getStyleName("filter", "keywords-title"));
        HTML titleHTML = new HTML(watch.getTranslation("filter.keywords.title"));
        titleHTML.setStyleName(watch.getStyleName("filter", "title-keywords-text"));
        p.add(titleHTML);
        return p;
    }

    public void resetSelections() {
        Iterator it = keywordsLink.keySet().iterator();
        String keywordactive = watch.getFilterStatus().getKeyword();
        String groupactive = watch.getFilterStatus().getGroup();
        while (it.hasNext()) {
            Keyword keyword  = (Keyword) it.next();
            KeywordItemObject kwIo = (KeywordItemObject)keywordsLink.get(keyword);
            if (keyword.getName().equals(keywordactive) 
                && keyword.getGroup().equals(groupactive)) {
                kwIo.setSelected(true);
            } else {
                kwIo.setSelected(false);
            }
        }
    }

    public void refreshData() {
        keywordsPanel.clear();
        keywordsLink.clear();
        List keywords = watch.getConfig().getKeywords();
        if (keywords!=null) {
            Iterator it = keywords.iterator();
            while (it.hasNext()) {
                final Keyword keyword = (Keyword) it.next();
                if ((keyword.getName()!=null)&&(!keyword.equals(""))) {
                    //get the widget
                    String kwKey = keyword.getPageName() + "-" + keyword.getGroup();
                    KeywordItemObject kwObject = new KeywordItemObject(kwKey, keyword);
                    keywordsLink.put(keyword, kwObject);
                    keywordsPanel.add(kwObject.getWidget(false));
                }
            }
        }
        resetSelections();
    }
    
    public class KeywordItemObject extends ItemObject {
        protected HyperlinkComposite widget;

        public KeywordItemObject(String key, Object data)
        {
            super(key, data);
        }
        
        public String getDisplayName() {
            Keyword keyword = (Keyword)this.data;
            Group kwGroup = (Group) watch.getConfig().getGroups()
                            .get(keyword.getGroup());
            String groupDisplayName;
            if (kwGroup == null) {
            groupDisplayName = keyword.getGroup();
            } else {
            groupDisplayName = kwGroup.getName();
            }
            String keywordDisplayName = keyword.getName() 
                + ((!groupDisplayName.trim().equals("")) ?  (" - " + groupDisplayName) : "");
            return keywordDisplayName;
        }

        public Widget getWidget(boolean selected)
        {
            final Keyword keyword = (Keyword)this.data;
            Hyperlink link = new Hyperlink(this.getDisplayName(), "");
            link.addStyleName(watch.getStyleName("keyword", "link"));
            link.addClickListener(new ClickListener() {
                public void onClick(Widget widget) {
                    watch.refreshOnActivateKeyword(keyword);
                }
            });
            widget = new HyperlinkComposite(link);
            widget.addStyleName(watch.getStyleName("keyword"));
            this.setSelected(selected);
            return widget;
        }
        
        /**
         * Ugly method to change the keyword widget associated to this keyword.
         * 
         * @param kwWidget the current widget of the keyword to change
         * @param selected wheather the item should be changed to selected or not
         */
        public void setSelected(boolean selected) {
            //remove all hyperlinks, if any
            for (Iterator wIt = this.widget.getHyperlinks().iterator(); wIt.hasNext();) {
                HyperlinkComposite w = (HyperlinkComposite)wIt.next();
                this.widget.remove(w);
            } 
            if (selected) {
                this.widget.addStyleName(watch.getStyleName("keyword","active"));
            } else {
                this.widget.removeStyleName(watch.getStyleName("keyword","active"));
            }
            final Keyword keyword = (Keyword)this.data;
            //now add some actions if needed
            if (selected && !keyword.getPageName().equals("")) {
                //create a composite with link as main widget and some actions
                Hyperlink editHyperlink = new Hyperlink(watch.getTranslation("keyword.edit"), "#");
                editHyperlink.addClickListener(new ClickListener() {
                    public void onClick (Widget widget) {
                        KeywordDialog kwDialog = new KeywordDialog(watch, "addkeyword", 
                            Dialog.BUTTON_CANCEL | Dialog.BUTTON_NEXT, keyword);
                        kwDialog.setAsyncCallback(new AsyncCallback() {
                            public void onFailure(Throwable throwable) {
                                //nothing
                            }
                            public void onSuccess(Object object) {
                                Keyword newKeyword = (Keyword)object;
                                watch.getDataManager().updateKeyword(newKeyword, new XWikiAsyncCallback(watch) {
                                    public void onFailure(Throwable caught) {
                                        super.onFailure(caught);
                                    }
    
                                    public void onSuccess(Object result) {
                                        super.onSuccess(result);
                                        //refresh on the new keyword
                                        watch.refreshOnNewKeyword();
                                        watch.refreshOnActivateKeyword(keyword);
                                    }
                                });
                            }
                        });
                        kwDialog.show();
                    }
                });
                HyperlinkComposite editHyperlinkComposite = new HyperlinkComposite(editHyperlink);
                Hyperlink deleteHyperlink = new Hyperlink(watch.getTranslation("keyword.delete"), "");
                deleteHyperlink.addClickListener(new ClickListener() {
                   public void onClick(Widget widget) {
                       String confirmString = watch.getTranslation("removekeyword.confirm", 
                           new String[] {KeywordItemObject.this.getDisplayName()});
                       boolean confirm = Window.confirm(confirmString);
                       if (confirm) {
                           watch.getDataManager().removeKeyword(keyword, new XWikiAsyncCallback(watch) {
                               public void onFailure(Throwable caught) {
                                   super.onFailure(caught);
                               }
                               public void onSuccess(Object result) {
                                   super.onSuccess(result);
                                   watch.refreshOnNewKeyword();
                                   //cancel the keyword selection
                                   watch.refreshOnActivateKeyword(new Keyword("", ""));
                               }
                           });
                       } else {
                           //nothing
                       }
                   } 
                });
                HyperlinkComposite deleteHyperlinkComposite = new HyperlinkComposite(deleteHyperlink);
                //set styles
                editHyperlinkComposite.setStyleName(watch.getStyleName("keyword", "keywordaction") 
                    + " " + watch.getStyleName("keyword", "editkeyword"));
                deleteHyperlinkComposite.setStyleName(watch.getStyleName("keyword", "keywordaction") 
                    + " " + watch.getStyleName("keyword", "deletekeyword"));
                //add the two actions to the hyperlink composite, in reverse order since they will
                //be floated to the right
                this.widget.add(deleteHyperlinkComposite);
                this.widget.add(editHyperlinkComposite);
            }
        }

        public HyperlinkComposite getWidget()
        {
            return widget;
        }
    }
}
