package com.mrprez.gencross.formula;

public abstract class FormulaSeme implements Cloneable {
	private FormulaSeme next;
	private FormulaSeme previous;
	protected Formula formula;
	
	
	public abstract FormulaSeme clone();
	public abstract boolean equals(Object object);
	public abstract int hashCode();
	
	public FormulaSeme getNext() {
		return next;
	}
	public void setNext(FormulaSeme next) {
		this.next = next;
		if(next!=null && next.getPrevious()!=this){
			next.setPrevious(this);
		}
	}
	
	public FormulaSeme getPrevious() {
		return previous;
	}
	public void setPrevious(FormulaSeme previous) {
		this.previous = previous;
		if(previous!=null && previous.getNext()!=this){
			previous.setNext(this);
		}
	}
	public Formula getFormula() {
		return formula;
	}
	public void setFormula(Formula formula) {
		this.formula = formula;
	}
	
	
	
	

}
