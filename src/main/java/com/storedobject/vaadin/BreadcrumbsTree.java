package com.storedobject.vaadin;

import com.storedobject.helper.ID;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Breadcrumb Tree component. It contains a tree of "Breadcrumbs" and each node {@link Node} of the tree can
 * hold a {@link Component} and some sort of ad-hoc data. From a specific branch of the tree, only the component
 * corresponding to the last expanded node is displayed and all other component in that branch hierarchy is hidden.
 * However, all the nodes, up to the last expanded node, are displayed as part of the breadcrumb and any node can be
 * clicked to jump to that node.
 *
 * @author Syam
 */
public class BreadcrumbsTree extends Composite<VerticalLayout> {

    private final VerticalLayout layout = new VerticalLayout();
    private final List<Node> roots = new ArrayList<>();
    private final List<NodeVisibilityListener> listeners = new ArrayList<>();

    /**
     * Constructor.
     */
    public BreadcrumbsTree() {
    }

    @Override
    protected VerticalLayout initContent() {
        return layout;
    }

    /**
     * Add a root node to this tree.
     *
     * @param caption Caption for the new root node.
     * @param component Component associated with the node.
     * @return Newly created nde.
     */
    public Node add(String caption, Component component) {
        Node node = createRoot(this, caption, component);
        roots.add(node);
        return node;
    }

    /**
     * Remove a node from this tree.
     *
     * @param node Node to be removed.
     * @return True if removed. False if it was not located and thus, not removed.
     */
    public boolean remove(Node node) {
        if(roots.remove(node)) {
            node.detach();
            return true;
        }
        for(Node n: roots) {
            if(n.remove(node)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the root nodes.
     *
     * @return Stream of root nodes.
     */
    public Stream<Node> getRoots() {
        return roots.stream();
    }

    /**
     * Visit all nodes.
     *
     * @param visitor The visitor who consumes each node.
     */
    public void visit(Consumer<Node> visitor) {
        roots.forEach(r -> consume(r, visitor));
    }

    private void consume(Node node, Consumer<Node> visitor) {
        visitor.accept(node);
        node.children.forEach(n -> consume(n, visitor));
    }

    /**
     * Clear this tree by removing all nodes and its associated data.
     */
    public void clear() {
        while(!roots.isEmpty()) {
            remove(roots.get(0));
        }
    }

    private static Node createRoot(BreadcrumbsTree tree, String caption, Component component) {
        Node root = tree.new Node(caption, component, null);
        tree.layout.add(root);
        if(component != null) {
            tree.layout.add(component);
        }
        return root;
    }

    private static void addAfter(BreadcrumbsTree tree, Node newOne, Node afterMe) {
        Component am = afterMe.component == null ? afterMe : afterMe.component;
        List<Component> after = new ArrayList<>();
        tree.layout.getChildren().dropWhile(c -> c != am).skip(1).forEach(after::add);
        after.forEach(tree.layout::remove);
        tree.layout.add(newOne);
        if(newOne.component != null) {
            tree.layout.add(newOne.component);
        }
        after.forEach(tree.layout::add);
    }

    /**
     * The node of the breadcrumb tree.
     *
     * @author Syam
     */
    public final class Node extends Composite<Breadcrumbs> {

        private final long id = ID.newID();
        private final Node parent;
        private final List<Node> children = new ArrayList<>();
        private final Breadcrumbs thisCrumb = new Breadcrumbs();
        private final Breadcrumbs.Breadcrumb thisBreadcrumb;
        private final Component component;
        private boolean expanded;
        private final String caption;
        private Object data;

        private Node(String caption, Component component, Node parent) {
            this.parent = parent;
            if(caption == null || caption.isBlank()) {
                caption = "Node " + id;
            }
            this.caption = caption;
            this.component = component;
            createParentCrumbs(parent);
            thisBreadcrumb = thisCrumb.add(caption);
            thisBreadcrumb.addClickListener(e -> {
                if(!children.isEmpty()) {
                    if(expanded) {
                        collapse();
                    } else {
                        expand();
                    }
                }
            });
        }

        private void createParentCrumbs(Node parent) {
            if(parent == null) {
                return;
            }
            createParentCrumbs(parent.parent);
            Breadcrumbs.Breadcrumb b = thisCrumb.add(parent.caption);
            b.getStyle().set("cursor", "pointer");
            b.addClickListener(e -> parent.collapse());
        }

        @Override
        protected Breadcrumbs initContent() {
            return thisCrumb;
        }

        /**
         * Get child nodes.
         *
         * @return Stream of child nodes.
         */
        public Stream<Node> getNodes() {
            return children.stream();
        }

        @Override
        public void setVisible(boolean visible) {
        }

        private void visible(boolean visible, boolean hideParent) {
            boolean wasVisible = isVisible();
            super.setVisible(visible);
            if(component != null) {
                component.setVisible(visible);
            }
            expanded = !visible;
            if(hideParent && parent != null) {
                hideParent();
            }
            if(isVisible() != wasVisible) {
                listeners.forEach(listener -> listener.nodeVisible(this, !wasVisible));
            }
        }

        private void hideParent() {
            if(parent != null) {
                parent.visible(false, false);
                parent.hideParent();
            }
        }

        private void visible(boolean visible) {
            visible(visible, true);
        }

        /**
         * Expand this node.
         */
        public void expand() {
            expand(true);
        }

        /**
         * Collapse this node.
         */
        public void collapse() {
            expand(false);
        }

        private void expand(boolean expand) {
            visible(!expand);
            visibleChildren(expand);
        }

        private void visibleChildren(boolean visible) {
            children.forEach(c -> {
                c.visible(visible, false);
                if(!visible) {
                    c.visibleChildren(false);
                }
            });
        }

        private Node tailEnd() {
            if(children.isEmpty()) {
                return this;
            }
            return children.get(children.size() - 1).tailEnd();
        }

        /**
         * Add a new node under this node.
         *
         * @param caption Caption of the new node.
         * @param component Component associated with the new node. If <code>null</code> is passed, no associated
         *                  component will be displayed when the node is selected.
         * @return The newly added node.
         */
        public Node add(String caption, Component component) {
            thisBreadcrumb.getStyle().set("cursor", "pointer");
            Node child = new Node(caption, component, this);
            addAfter(BreadcrumbsTree.this, child, tailEnd());
            children.add(child);
            if(!isTreeExpanded()) {
                child.visible(false, false);
            }
            return child;
        }

        private boolean isTreeExpanded() {
            if(!expanded) {
                return false;
            }
            if(parent == null) {
                return true;
            }
            return parent.isTreeExpanded();
        }

        /**
         * Remove a child node from this node or from one of its descendants.
         *
         * @param node Node to remove.
         * @return True if removed. Otherwise, false.
         */
        public boolean remove(Node node) {
            if(children.remove(node)) {
                node.detach();
                if(children.isEmpty() && isTreeExpanded()) {
                    expanded = false;
                    expand();
                }
                if(children.isEmpty()) {
                    thisBreadcrumb.getStyle().remove("cursor");
                }
                return true;
            }
            for(Node c: children) {
                if(c.remove(node)) {
                    return true;
                }
            }
            return false;
        }

        private void detach() {
            children.forEach(Node::detach);
            layout.remove(this);
            if(component != null) {
                layout.remove(component);
            }
        }

        @Override
        public boolean equals(Object o) {
            return this == o;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }

        /**
         * Get the caption of this node.
         *
         * @return Caption.
         */
        public String getCaption() {
            return caption;
        }

        /**
         * Get the component associated with this node.
         *
         * @return Associated component. The associated component will be displayed when the node is displayed,
         * otherwise, it will be hidden.
         */
        public Component getComponent() {
            return component;
        }

        /**
         * Get the ad-hoc data associated with this node.
         *
         * @return Ad-hoc data.
         */
        public Object getData() {
            return data;
        }

        /**
         * Associate some ad-hoc data with this node.
         *
         * @param data Data to keep.
         */
        public void setData(Object data) {
            this.data = data;
        }
    }

    /**
     * Add a {@link NodeVisibilityListener}.
     *
     * @param listener Listener.
     * @return Registration instance that can be used to remove the listener.
     */
    public Registration addNodeVisibilityListener(NodeVisibilityListener listener) {
        assert listener != null;
        listeners.add(listener);
        return () -> listeners.remove(listener);
    }

    /**
     * Listener that can be added to the {@link BreadcrumbsTree} for monitoring the visibility changes of the nodes
     * (and its associated components).
     *
     * @author Syam
     */
    public interface NodeVisibilityListener {

        /**
         * This method will be invoked when the visibility of the node (and its associated component) changes.
         *
         * @param node Node that is changed.
         * @param visible Whether it became visible or not.
         */
        void nodeVisible(Node node, boolean visible);
    }
}
