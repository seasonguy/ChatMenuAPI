package me.tom.sparse.spigot.chat.menu.element;

import me.tom.sparse.spigot.chat.menu.ChatMenu;
import me.tom.sparse.spigot.chat.menu.ChatMenuAPI;
import me.tom.sparse.spigot.chat.util.State;
import me.tom.sparse.spigot.chat.util.Text;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VerticalSelectorElement extends Element
{
	protected static final int SELECTED_PREFIX_WIDTH = ChatMenuAPI.getWidth("> ");
	
	protected String[] options;
	protected int      width;
	
	//	protected int selectedIndex;
	protected State<Integer> value;
	
	protected ChatColor selectedColor = ChatColor.GREEN;
	
	public VerticalSelectorElement(int x, int y, int defaultSelected, String... options)
	{
		super(x, y);
		this.value = new State<>(defaultSelected, this::filter);
		for(String option : options)
		{
			if(option.contains("\n"))
				throw new IllegalArgumentException("Option cannot contain newline");
			
			int w = ChatMenuAPI.getWidth(option);
			if(w > width)
				width = w;
		}
		
		this.options = options;
	}
	
	private int filter(int v)
	{
		return Math.max(Math.min(v, options.length), 0);
	}
	
	public void setSelectedColor(ChatColor selectedColor)
	{
		this.selectedColor = selectedColor;
	}
	
	public ChatColor getSelectedColor()
	{
		return selectedColor;
	}
	
	public void setSelectedIndex(int value)
	{
		this.value.set(value);
	}
	
	public int getSelectedIndex()
	{
		return value.current();
	}
	
	public String getSelectedOption()
	{
		int selectedIndex = value.current();
		return selectedIndex >= 0 && selectedIndex < options.length ? options[selectedIndex] : null;
	}
	
	public int getWidth()
	{
		return SELECTED_PREFIX_WIDTH + width;
	}
	
	public int getHeight()
	{
		return options.length;
	}
	
	public boolean isEnabled()
	{
		return true;
	}
	
	public List<Text> render(ChatMenu menu, int elementIndex)
	{
		String baseCommand = menu.getCommand() + elementIndex + " ";
		
		List<Text> result = new ArrayList<>();
		for(int i = 0; i < options.length; i++)
		{
			Text text = new Text();
			BaseComponent[] components = TextComponent.fromLegacyText(options[i]);
			
			if(i == value.current())
			{
				text.append("> ");
				if(selectedColor != null)
					for(BaseComponent component : components)
						component.setColor(selectedColor);
			}else
			{
				text.expandToWidth(SELECTED_PREFIX_WIDTH);
				ClickEvent click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, baseCommand + i);
				for(BaseComponent component : components)
					component.setClickEvent(click);
			}
			text.append(components);
			result.add(text);
		}
		return result;
	}
	
	public void edit(ChatMenu menu, String[] args)
	{
		value.set(Integer.parseInt(args[0]));
	}
	
	public List<State<?>> getStates()
	{
		return Collections.singletonList(value);
	}
}
