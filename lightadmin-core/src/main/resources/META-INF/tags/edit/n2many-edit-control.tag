<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<%@ attribute name="attributeMetadata" required="true"
			type="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="disabled" required="false" type="java.lang.Boolean" %>
<select name="${attributeMetadata.name}" multiple="multiple" class="chzn-select" data-placeholder=" " ${disabled ? 'disabled' : ''}>
	<light:domain-type-elements domainType="${attributeMetadata.elementType}" idVar="elementId"
								stringRepresentationVar="elementName">
		<option value="${elementId}"><c:out value="${elementName}" escapeXml="true"/></option>
	</light:domain-type-elements>
</select>
