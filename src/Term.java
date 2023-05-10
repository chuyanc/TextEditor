public class Term implements ITerm {

    private String term;
    private long weight;

    /**
     * Initialize a Term with a given query String and weight
     */
    public Term(String term, long weight) {
        if (term == null || weight < 0) {
            throw new IllegalArgumentException();
        }
        this.setTerm(term);
        this.setWeight(weight);
    }

    @Override
    public int compareTo(ITerm that) {
        return this.getTerm().compareTo(that.getTerm());
    }

    @Override
    public String toString() {
        return this.getWeight() + "\t" + this.getTerm();
    }

    @Override
    public long getWeight() {
        return this.weight;
    }

    @Override
    public String getTerm() {
        return this.term;
    }

    @Override
    public void setWeight(long weight) {
        this.weight = weight;
    }

    @Override
    public void setTerm(String term) {
        this.term = term;
    }

}
