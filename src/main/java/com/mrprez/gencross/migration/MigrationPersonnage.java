package com.mrprez.gencross.migration;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.dom4j.Element;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.formula.MalformedFormulaException;
import com.mrprez.gencross.history.HistoryFactory;
import com.mrprez.gencross.listener.dummy.DummyAfterAddPropertyListener;
import com.mrprez.gencross.listener.dummy.DummyAfterChangeValueListener;
import com.mrprez.gencross.listener.dummy.DummyAfterDeletePropertyListener;
import com.mrprez.gencross.listener.dummy.DummyBeforeAddPropertyListener;
import com.mrprez.gencross.listener.dummy.DummyBeforeChangeValueListener;
import com.mrprez.gencross.listener.dummy.DummyBeforeDeletePropertyListener;
import com.mrprez.gencross.listener.dummy.DummyPassToNextPhaseListener;
import com.mrprez.gencross.renderer.Renderer;

public class MigrationPersonnage extends Personnage {
	
	
	public MigrationPersonnage(Element root, PluginDescriptor pluginDescriptor) throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException, MalformedFormulaException, InvocationTargetException{
		super();
		setPluginDescriptor(pluginDescriptor);
		setXML(root);
	}

	@Override
	public void setXMLListeners(Element root) throws Exception {
		beforeChangeValueListeners.clear();
		Iterator<?> it = root.element("beforeChangeValueListeners").elementIterator("listener");
		while(it.hasNext()){
			Element listenerEl = (Element)it.next();
			beforeChangeValueListeners.add(new DummyBeforeChangeValueListener(listenerEl, this));
		}
		
		afterChangeValueListeners.clear();
		it = root.element("afterChangeValueListeners").elementIterator("listener");
		while(it.hasNext()){
			Element listenerEl = (Element)it.next();
			afterChangeValueListeners.add(new DummyAfterChangeValueListener(listenerEl, this));
		}
		
		beforeAddPropertyListeners.clear();
		it = root.element("beforeAddPropertyListeners").elementIterator("listener");
		while(it.hasNext()){
			Element listenerEl = (Element)it.next();
			beforeAddPropertyListeners.add(new DummyBeforeAddPropertyListener(listenerEl, this));
		}
		
		afterAddPropertyListeners.clear();
		it = root.element("afterAddPropertyListeners").elementIterator("listener");
		while(it.hasNext()){
			Element listenerEl = (Element)it.next();
			afterAddPropertyListeners.add(new DummyAfterAddPropertyListener(listenerEl, this));
		}
		
		beforeDeletePropertyListeners.clear();
		it = root.element("beforeDeletePropertyListeners").elementIterator("listener");
		while(it.hasNext()){
			Element listenerEl = (Element)it.next();
			beforeDeletePropertyListeners.add(new DummyBeforeDeletePropertyListener(listenerEl, this));
		}
		
		afterDeletePropertyListeners.clear();
		it = root.element("afterDeletePropertyListeners").elementIterator("listener");
		while(it.hasNext()){
			Element listenerEl = (Element)it.next();
			afterDeletePropertyListeners.add(new DummyAfterDeletePropertyListener(listenerEl, this));
		}
		
		passToNextPhaseListeners.clear();
		if(root.element("passToNextPhaseListeners")!=null){
			it = root.element("passToNextPhaseListeners").elementIterator("listener");
			while(it.hasNext()){
				Element listenerEl = (Element)it.next();
				passToNextPhaseListeners.add(new DummyPassToNextPhaseListener(listenerEl, this));
			}
		}
	}
	
	
	
	
	@Override
	public Renderer getRenderer(String className){
		return new DummyRenderer(className);
	}
	
	@Override
	public Class<? extends HistoryFactory> getHistoryFactoryClass(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return DummyHistoryFactory.class;
	}

	

}
