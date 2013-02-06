package org.opentides.web.json;

public interface Views {
	static class BaseView {}
    static class SearchView  extends BaseView {}
    static class FormView    extends SearchView {}
    static class DisplayView extends FormView {}
    static class FullView    extends DisplayView {}    
}
