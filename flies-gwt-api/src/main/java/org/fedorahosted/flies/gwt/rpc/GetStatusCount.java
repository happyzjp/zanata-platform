package org.fedorahosted.flies.gwt.rpc;

import net.customware.gwt.dispatch.shared.Action;

import org.fedorahosted.flies.gwt.model.DocumentId;
import org.fedorahosted.flies.gwt.model.LocaleId;
import org.fedorahosted.flies.gwt.model.TransUnitId;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GetStatusCount implements Action<GetStatusCountResult>, IsSerializable{

	private static final long serialVersionUID = -1218943735746130251L;

	private DocumentId documentId;
	private LocaleId localeId;

	@SuppressWarnings("unused")
	public GetStatusCount(){
	}
	
	public GetStatusCount(DocumentId id) {
		this.documentId = id;
	}

	public DocumentId getDocumentId() {
		return documentId;
	}
	
	public void setDocumentId(DocumentId documentId) {
		this.documentId = documentId;
	}

	public LocaleId getLocaleId() {
		// TODO Auto-generated method stub
		return localeId;
	}
}
