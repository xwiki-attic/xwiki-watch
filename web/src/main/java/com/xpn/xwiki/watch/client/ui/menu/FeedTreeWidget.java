package com.xpn.xwiki.watch.client.ui.menu;

import com.xpn.xwiki.watch.client.ui.WatchWidget;
import com.xpn.xwiki.watch.client.ui.dialog.FeedDialog;
import com.xpn.xwiki.watch.client.ui.dialog.StandardFeedDialog;
import com.xpn.xwiki.watch.client.Watch;
import com.xpn.xwiki.watch.client.Feed;
import com.xpn.xwiki.watch.client.Constants;
import com.xpn.xwiki.gwt.api.client.app.XWikiAsyncCallback;
import com.xpn.xwiki.gwt.api.client.dialog.Dialog;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

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

public class FeedTreeWidget  extends WatchWidget {
    private Tree groupTree = new Tree();
    public FeedTreeWidget() {
        super();
    }

    public String getName() {
        return "feedtree";
    }
    
    public FeedTreeWidget(Watch watch) {
        super(watch);
        setPanel(new FlowPanel());
        initWidget(panel);
        init();
    }

    public void init() {
        super.init();
        HTML titleHTML = new HTML(watch.getTranslation("feedtree.title"));
        titleHTML.setStyleName(watch.getStyleName("feedtree", "title"));
        titleHTML.addClickListener(new ClickListener() {
            public void onClick(Widget widget) {
                watch.launchConfig("feeds");
            }
        });
        panel.add(titleHTML);

        Image configImage = new Image(watch.getSkinFile(Constants.IMAGE_CONFIG));
        configImage.setStyleName(watch.getStyleName("feedtree", "image"));
        configImage.setTitle(watch.getTranslation("config"));
        configImage.addClickListener(new ClickListener() {
            public void onClick(Widget widget) {
                watch.launchConfig("feeds");
            }
        });
        panel.add(configImage);
        groupTree.setStyleName(watch.getStyleName("feedtree","groups"));
        panel.add(groupTree);
    }

    public void refreshData() {
        // we need to make sure the feed tree has been prepared
        makeFeedTree();
    }

    private void  makeFeedTree() {
        //get the state of the tree items on first level -> the groups tree items
        HashMap itemsState = new HashMap();
        for (int i = 0; i < this.groupTree.getItemCount(); i++) {
            TreeItem currentTreeItem = this.groupTree.getItem(i);
            //get user object
            TreeItemObject userObj = (TreeItemObject)currentTreeItem.getUserObject();
            itemsState.put(userObj.getKey(), new Boolean(currentTreeItem.getState()));
        }
        //get the selected item to set it back when the tree is refreshed 
        TreeItem selectedTreeItem = this.groupTree.getSelectedItem();
        String selectedItemKey = null;
        if (selectedTreeItem != null) {
            TreeItemObject selectedItemObject = (TreeItemObject)selectedTreeItem.getUserObject();
            if (selectedItemObject != null) {
                selectedItemKey = selectedItemObject.getKey();
            }
        }
        // clear all trees
        groupTree.clear();

        Map feedsbygroup = watch.getConfig().getFeedsByGroupList();
        Map groups = watch.getConfig().getGroups();

        List keys = new ArrayList(feedsbygroup.keySet());
        Collections.sort(keys);
        Iterator groupit = keys.iterator();
        while (groupit.hasNext()) {
            final String groupname = (String) groupit.next();
            String groupTitle = (String) groups.get(groupname);
            if (groupTitle==null)
             groupTitle = groupname;
            if ((groupname!=null)&&(!groupname.trim().equals(""))) {
                Map group = (Map) feedsbygroup.get(groupname);
                TreeItem groupItemTree = new TreeItem();
                //set the TreeItem's object
                GroupTreeItemObject groupObj = new GroupTreeItemObject(groupname, groupTitle);
                groupItemTree.setUserObject(groupObj);
                //check if selected
                boolean selected = false;
                if (selectedItemKey != null && groupname.equals(selectedItemKey)) {
                    selected = true;
                    selectedTreeItem = groupItemTree;
                }
                groupItemTree.setWidget(groupObj.getWidget(selected));
                groupTree.addItem(groupItemTree);
                List feedList = new ArrayList(group.keySet());
                Collections.sort(feedList);
                Iterator feedgroupit = feedList.iterator();
                while (feedgroupit.hasNext()) {
                    String feedname = (String) feedgroupit.next();
                    Feed feed = (Feed) group.get(feedname);
                    //set it's userObject to the name of the group + name of the feed since a 
                    //feed can be part of multiple groups and we need to identify it uniquely.
                    String itemTreeKey = groupname + "." + feedname;
                    TreeItemObject feedObj = new FeedTreeItemObject(itemTreeKey, feed);
                    TreeItem feedItem = new TreeItem();
                    feedItem.setUserObject(feedObj);
                    selected = false;
                    if (selectedItemKey != null && itemTreeKey.equals(selectedItemKey)) {
                        selected = true;
                        selectedTreeItem = feedItem;
                    }
                    feedItem.setWidget(feedObj.getWidget(selected));
                    groupItemTree.addItem(feedItem);
                }
                //expand it if necessary
                Boolean state = (Boolean)itemsState.get(groupname);
                if (state != null) {
                    groupItemTree.setState(state.booleanValue());
                }
                groupTree.addItem(groupItemTree);
            }
        }
        //set the selected tree item
        this.groupTree.setSelectedItem(selectedTreeItem);
    }

    public void resizeWindow() {
        // Watch.setMaxHeight(panel);
    }

    /**
     * Class to enclose data to be stored by a tree item that should be able to:
     * <ul>
     * <li> get uniquely identifying key for this tree item </li>
     * <li> generate a widget to be displayed by the tree item </li>
     * </ul>
     */
    public abstract class TreeItemObject {
        //the unique ID 
        protected String key;
        //the data of the TreeItemObject
        protected Object data; 
        
        public TreeItemObject(String key, Object data)
        {
            this.key = key;
            this.data = data;
        }
        
        public String getKey() {
            return this.key;
        }
        
        public abstract Widget getWidget(boolean selected);
    }
    
    public class GroupTreeItemObject extends TreeItemObject {
        public GroupTreeItemObject(String key, Object data)
        {
            super(key, data);
        }

        public GroupTreeItemObject(String groupname, String groupTitle)
        {
            super(groupname, null);
            //can only instantiate groupdata obj from class.
            //TODO: declare it static or not inner once all the data classes are moved.
            this.data = new String[2];
            ((String[])this.data)[0] = groupname;
            ((String[])this.data)[1] = groupTitle;
        }

        public Widget getWidget(boolean selected)
        {
            final String[] gData = (String[])this.data;
            Hyperlink link = new Hyperlink(gData[1], "");
            link.setStyleName(watch.getStyleName("feedtree","link"));
            link.addClickListener(new ClickListener() {
                public void onClick(Widget widget) {
                    watch.refreshOnGroupChange(gData[0]);
                }
            });            
            Widget widget = link;
            if (selected) {
                //create a composite with link as main widget and some actions
                widget = new HyperlinkComposite(link);
                //no actions for now
            }
            return widget;
        }
    }
    
    public class FeedTreeItemObject extends TreeItemObject {

        public FeedTreeItemObject(String key, Object data)
        {
            super(key, data);
        }
        
        private String getFavIcon(Feed feed) {
            return watch.getFavIcon(feed);
        }

        public Widget getWidget(boolean selected)
        {
            //cast data to feed
            final Feed feed = (Feed)this.data;
            String feedtitle =  feed.getName() + "(" + feed.getNb() + ")";
            String imgurl = getFavIcon(feed);     
            if (imgurl!=null)
             feedtitle = "<img src=\"" + imgurl + "\" class=\"" + watch.getStyleName("feedtree","logo-icon") + "\" alt=\"\" />" + feedtitle;
            Hyperlink link = new Hyperlink(feedtitle, true, "");
            link.addClickListener(new ClickListener() {
                public void onClick(Widget widget) {
                    watch.refreshOnFeedChange(feed);
                } 
            });
            link.setStyleName(watch.getStyleName("feedtree","link"));            
            Widget widget = link;
            
            //if selected, generate the two action links
            if (selected) {
                widget = new HyperlinkComposite(link);
                //create the inner item
                Hyperlink editHyperlink = new Hyperlink("edit", "");
                editHyperlink.addClickListener(new ClickListener() {
                    public void onClick (Widget widget) {
                        FeedDialog feedDialog = new StandardFeedDialog(watch, "standardfeed", Dialog.BUTTON_CANCEL | Dialog.BUTTON_NEXT, feed);
                        feedDialog.setAsyncCallback(new AsyncCallback() {
                            public void onFailure(Throwable throwable) {
                                //nothing
                            }
                            public void onSuccess(Object object) {
                                Feed newfeed = (Feed) object;
                                watch.getDataManager().updateFeed(newfeed, new XWikiAsyncCallback(watch) {
                                    public void onFailure(Throwable caught) {
                                        super.onFailure(caught);
                                    }
    
                                    public void onSuccess(Object result) {
                                        super.onSuccess(result);
                                        // We need to refreshData the tree
                                        watch.refreshOnNewFeed();
                                    }
                                });
                            }
                        });
                        feedDialog.show();                    
                    }
                });
                ((HyperlinkComposite)widget).add(new HyperlinkComposite(editHyperlink));
                Hyperlink deleteHyperlink = new Hyperlink("delete", "");
                deleteHyperlink.addClickListener(new ClickListener() {
                   public void onClick(Widget widget) {
                       String confirmString = watch.getTranslation("removefeed.confirm", 
                                                                   new String[] {feed.getName()});
                       boolean confirm = com.google.gwt.user.client.Window.confirm(confirmString);
                       if (confirm) {
                           watch.getDataManager().removeFeed(feed, new XWikiAsyncCallback(watch) {
                               public void onFailure(Throwable caught) {
                                   super.onFailure(caught);
                               }
                               public void onSuccess(Object result) {
                                   super.onSuccess(result);
                                   // We need to refreshData the tree
                                   watch.refreshOnNewFeed();
                               }
                           });
                       } else {
                           //nothing
                       }
                   } 
                });
                ((HyperlinkComposite)widget).add(new HyperlinkComposite(deleteHyperlink));
            }
            return widget;
        }
    }
}
