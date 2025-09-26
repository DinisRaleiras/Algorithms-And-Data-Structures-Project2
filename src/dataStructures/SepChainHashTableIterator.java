package dataStructures;

public class SepChainHashTableIterator<K extends Comparable<K>, V> implements Iterator<Entry<K, V>> {

	static final long serialVersionUID = 0L;

	private int pos;
	private Iterator<Entry<K, V>> currIterator;
	private Dictionary<K, V>[] table;

	public SepChainHashTableIterator(Dictionary<K, V>[] table) {
		this.table = table;
		rewind();
	}

	@Override
	public boolean hasNext() {
		return currIterator != null && currIterator.hasNext();
	}

	@Override
	public Entry<K, V> next() throws NoSuchElementException {
		if (!this.hasNext()) {
			throw new NoSuchElementException();
		}

		Entry<K, V> entry = currIterator.next();

		if (!currIterator.hasNext())
			findNonEmptySlot();

		return entry;
	}

	@Override
	public void rewind() {
		pos = 0;
		findNonEmptySlot();
	}

	private void findNonEmptySlot() {
		while (pos < table.length &&  table[pos].isEmpty())
			pos++;

		if (pos < table.length) {
			currIterator = table[pos].iterator();
			pos++;
		} else
			currIterator = null;
	}
}
