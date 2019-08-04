<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx }/js/jquery-1.8.3.js" type="text/javascript"></script>
<script src="${ctx }/js/common.js" type="text/javascript"></script>
<script src="${ctx }/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript">
	var ctx = '<%=request.getContextPath() %>';
</script>

