package com.storedobject.vaadin;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;

import java.util.HashMap;
import java.util.Map;

/**
 * Enhancements to Vaadin's Icon to handle other collections in a generic way.
 *
 * @author Syam
 */
public class Icon extends Composite<Component> implements HasStyle, HasIcon, HasSquareElement, ClickNotifier<Icon> {

    private static final Map<String, Class<? extends IconInterface>> iconCollections = new HashMap<>();
    static {
        iconCollections.put("vaadin", VIcon.class);
        iconCollections.put("icons", IIcon.class);
        iconCollections.put("av", IIcon.class);
        iconCollections.put("communication", IIcon.class);
        iconCollections.put("device", IIcon.class);
        iconCollections.put("editor", IIcon.class);
        iconCollections.put("hardware", IIcon.class);
        iconCollections.put("image", IIcon.class);
        iconCollections.put("maps", IIcon.class);
        iconCollections.put("notification", IIcon.class);
        iconCollections.put("social", IIcon.class);
        iconCollections.put("places", IIcon.class);
    }
    final IconInterface icon;

    /**
     * Create from a Vaadin icon
     * @param icon Vaadin icon
     */
    public Icon(VaadinIcon icon) {
        this(create(icon));
    }

    /**
     * Create from an icon from the name. It could specify the collection name too. See {@link #setIcon(String)}.
     *
     * @param icon Name of the Iron icon
     */
    public Icon(String icon) {
        this(create(icon));
    }

    private Icon(IconInterface icon) {
        this.icon = icon;
    }

    @Override
    protected Component initContent() {
        return (Component) icon;
    }

    @Override
    public Element getInternalElement() {
        return icon.getElement();
    }

    @Override
    public void setIcon(VaadinIcon icon) {
        setIcon(HasIcon.getIconName(icon));
    }

    @Override
    public void setIcon(String icon) {
        if(icon == null || icon.isBlank()) {
            return;
        }
        icon = HasIcon.getIconName(icon);
        Class<? extends IconInterface> iClass = iconCollections.get(icon.substring(0, icon.lastIndexOf(':')));
        if(this.icon.getClass() == iClass) {
            this.icon.setIcon(icon);
        }
    }

    private static IconInterface create(VaadinIcon icon) {
        IconInterface ii = new VIcon();
        ii.setIcon(icon);
        return ii;
    }

    private static IconInterface create(String icon) {
        IconInterface ii;
        if(icon == null || icon.isBlank()) {
            return new EmptyIcon();
        } else {
            icon = HasIcon.getIconName(icon);
            if(icon == null || icon.isBlank()) {
                return new EmptyIcon();
            }
            Class<? extends IconInterface> iClass = iconCollections.get(icon.substring(0, icon.lastIndexOf(':')));
            if(iClass == null) {
                return new EmptyIcon();
            }
            ii = create(iClass);
            if(ii instanceof VIcon) {
                String name = icon.substring(icon.lastIndexOf(':') + 1).toUpperCase()
                        .replace('-', '_');
                boolean found = false;
                for(VaadinIcon vi: VaadinIcon.values()) {
                    if(vi.name().equals(name)) {
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    return new EmptyIcon();
                }
            }
            ii.setIcon(icon);
        }
        return ii;
    }

    private static IconInterface create(Class<?> klass) {
        if(klass == IIcon.class) {
            return new IIcon();
        }
        return new VIcon();
    }

    @Override
    public String getIcon() {
        return icon.getIcon();
    }

    @Override
    public void setSize(String size) {
        HasSquareElement.super.setSize(size);
        icon.setSize(size);
    }

    @Override
    public String getSize() {
        return icon.getSize();
    }

    @Override
    public void setWidth(String width) {
        HasSquareElement.super.setWidth(width);
        icon.setWidth(width);
    }

    @Override
    public void setMinWidth(String minWidth) {
        HasSquareElement.super.setMinWidth(minWidth);
        icon.setMinWidth(minWidth);
    }

    @Override
    public void setMaxWidth(String maxWidth) {
        HasSquareElement.super.setMaxWidth(maxWidth);
        icon.setMaxWidth(maxWidth);
    }

    @Override
    public String getWidth() {
        return icon.getWidth();
    }

    @Override
    public String getMinWidth() {
        return icon.getMinWidth();
    }

    @Override
    public String getMaxWidth() {
        return icon.getMaxWidth();
    }

    @Override
    public void setHeight(String height) {
        HasSquareElement.super.setHeight(height);
        icon.setHeight(height);
    }

    @Override
    public void setMinHeight(String minHeight) {
        HasSquareElement.super.setMinHeight(minHeight);
        icon.setMinHeight(minHeight);
    }

    @Override
    public void setMaxHeight(String maxHeight) {
        HasSquareElement.super.setMaxHeight(maxHeight);
        icon.setMaxHeight(maxHeight);
    }

    @Override
    public String getHeight() {
        return icon.getHeight();
    }

    @Override
    public String getMinHeight() {
        return icon.getMinHeight();
    }

    @Override
    public String getMaxHeight() {
        return icon.getMaxHeight();
    }

    /**
     * Set the color.
     *
     * @param color Color to set.
     */
    public void setColor(String color) {
        icon.setColor(color);
    }

    /**
     * Get the current color.
     *
     * @return Current color.
     */
    public String getColor() {
        return icon.getColor();
    }

    /**
     * Set a style value.
     *
     * @param name Name of the style.
     * @param value Value of the style.
     */
    public void setStyle(String name, String value) {
        icon.getStyle().set(name, value);
    }

    /**
     * Set an attribute value.
     *
     * @param name Name of the attribute.
     * @param value Value of the attribute.
     */
    public void setAttribute(String name, String value) {
        icon.setAttribute(name, value);
    }

    /**
     * Add click handler.
     *
     * @param clickHandler Click handler
     * @return Registration.
     */
    public Registration addClickHandler(ClickHandler clickHandler) {
        ClickNotifier<?> clickNotifier = (ClickNotifier<?>) icon;
        return clickNotifier.addClickListener(e -> {
            @SuppressWarnings({"rawtypes", "unchecked"})
            ClickEvent<Icon> ce = new ClickHandler.ModifiedClickEvent(e, this);
            clickHandler.onComponentEvent(ce);
        });
    }

    @Override
    public Registration addClickListener(ComponentEventListener<ClickEvent<Icon>> listener) {
        ClickNotifier<?> clickNotifier = (ClickNotifier<?>) icon;
        return clickNotifier.addClickListener(e -> {
            @SuppressWarnings({"rawtypes", "unchecked"})
            ClickEvent<Icon> ce = new ClickHandler.ModifiedClickEvent(e, this);
            listener.onComponentEvent(ce);
        });
    }

    private static class VIcon extends com.vaadin.flow.component.icon.Icon implements IconInterface {
    }

    @NpmPackage(value = "@polymer/iron-icons", version = "^3.0.1")
    @JsModule("@polymer/iron-icons/iron-icons.js")
    @JsModule("@polymer/iron-icons/av-icons.js")
    @JsModule("@polymer/iron-icons/communication-icons.js")
    @JsModule("@polymer/iron-icons/device-icons.js")
    @JsModule("@polymer/iron-icons/editor-icons.js")
    @JsModule("@polymer/iron-icons/hardware-icons.js")
    @JsModule("@polymer/iron-icons/image-icons.js")
    @JsModule("@polymer/iron-icons/maps-icons.js")
    @JsModule("@polymer/iron-icons/notification-icons.js")
    @JsModule("@polymer/iron-icons/social-icons.js")
    @JsModule("@polymer/iron-icons/places-icons.js")
    @Tag("iron-icon")
    private static class IIcon extends Component implements IconInterface, ClickNotifier<IIcon> {
    }

    private static class EmptyIcon extends Span implements IconInterface {
    }

    private interface IconInterface extends HasStyle, HasIcon, HasSquareElement {

        default void setColor(String color) {
            if (color == null) {
                this.getStyle().remove("fill");
            } else {
                this.getStyle().set("fill", color);
            }
        }

        default String getColor() {
            return this.getStyle().get("fill");
        }

        @Override
        default Element getInternalElement() {
            return HasStyle.super.getInternalElement();
        }
    }
}
