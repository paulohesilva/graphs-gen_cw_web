package br.com.grafos.algoritmo.cw.model;

public class Arc implements Comparable<Arc>{

	private int i;
	
	private int j;
	
	private Double saving;
	
	public Arc(int i, int j, Double saving) {
		this.i = i;
		this.j = j;
		this.saving = saving;
	}
	
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public int getJ() {
		return j;
	}
	public void setJ(int j) {
		this.j = j;
	}
	public Double getSaving() {
		return saving;
	}
	public void setSaving(Double saving) {
		this.saving = saving;
	}

	@Override
	public int compareTo(Arc arc) {
		
		if(this.saving < arc.saving){
			return 1;
		}

		if(this.saving > arc.saving){
			return -1;
		}
		
		return 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + i;
		result = prime * result + j;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arc other = (Arc) obj;
		if ((i == other.i && j == other.j)||(i == other.j && j == other.i))
			return true;		
		else 
			return false;
				
	}
	
	@Override
	public String toString() {
		return "from --> "+ i + " TO ---> " + j + " saving is "+saving;
	}
	
	
}
