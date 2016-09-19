<!DOCTYPE html>
<html lang="en-US">
<head>
	<meta charset="UTF-8" />
	<title> Organizational structure manager </title>
</head>
<body ng-app="myApp">
<div ng-controller="CompanyController as companyCtrl">
	<h1> Organizational structure manager </h1>
	<form name="companyForm" method="POST">
		<table>
			<tr><td colspan="2">
				<div ng-if="companyCtrl.flag != 'edit'">
					<h3> Add New Company </h3>
				</div>
				<div ng-if="companyCtrl.flag == 'edit'">
					<h3> Update Company: {{ companyCtrl.company.name }} </h3>
				</div> </td>
			</tr>
			<tr>
				<td>Name: </td> <td><input type="text" name="name" ng-model="companyCtrl.company.name" required/>
				<span ng-show="companyForm.name.$error.required" class="msg-val">Name is required.</span> </td>
			</tr>
			<tr>
				<td>Estimated Earnings: </td> <td> <input type="text" name="selfEstimatedEarnings" ng-model="companyCtrl.company.selfEstimatedEarnings" required/>
				<span ng-show="companyForm.selfEstimatedEarnings.$error.required" class="msg-val">Estimated Earnings is required.</span> </td>
			</tr>
			<tr>
				<td ng-if="isSub">Parent Company Name:</td> <td ng-if="isSub"> <input type="text" name="parentsName" ng-model="companyCtrl.company.parentsName" required/>
				<span ng-show="companyForm.name.$error.required" class="msg-val">Parent Company Name is required.</span> </td>
			</tr>
			<tr>
				<td><input type="checkbox" ng-model="isSub"/>This is SubCompany</td>
			</tr>
			<tr>
				<td colspan="2"> <span ng-if="companyCtrl.flag=='created'" class="msg-success">Company successfully added.</span>
					<span ng-if="companyCtrl.flag=='failed'" class="msg-val">This name is already in use</span> </td>
			</tr>
			<tr><td colspan="2">
				<div ng-if="companyCtrl.flag != 'edit' && !isSub">
					<input  type="submit" ng-click="companyCtrl.addCompany()" value="Add As Parent Company"/>
					<input type="button" ng-click="companyCtrl.reset()" value="Reset"/>
				</div>
				<div ng-if="companyCtrl.flag == 'edit'">
					<input  type="submit" ng-click="companyCtrl.updateCompanyDetail()" value="Update Company"/>
					<input type="button" ng-click="companyCtrl.cancelUpdate()" value="Cancel"/>
				</div>
				<div ng-if="isSub && companyCtrl.flag != 'edit'">
					<input  type="submit" ng-click="companyCtrl.addCompany()" value="Add As Sub Company"/>
					<input type="button" ng-click="companyCtrl.reset()" value="Reset"/>
				</div></td>
			</tr>
			<tr>
				<td colspan="2"> <span ng-if="companyCtrl.flag=='deleted'" class="msg-success">Company successfully deleted.</span>
			</tr>
		</table>
	</form>
	<table>
		<tr><th>Name</th> <th>|Self Estimated Earnings</th><th>|Total Estimated Earnings</th></tr>
		<tr ng-repeat="row in companyCtrl.companies">
			<!--<td><span ng-bind="row.visual"></span></td> -->
			<td> <span ng-bind="row.visual"></span><span ng-bind="row.name"></span> </td>

			<td><span ng-bind="row.selfEstimatedEarnings"></span></td>
			<td><span ng-bind="row.totalEstimatedEarnings"></span></td>
			<td>
				<input type="button" ng-click="companyCtrl.deleteCompany(row.id)" value="Delete"/>
				<input type="button" ng-click="companyCtrl.editCompany(row.id)" value="Edit"/>
				<span ng-if="companyCtrl.flag=='updated' && row.id==companyCtrl.updatedId" class="msg-success">Company successfully updated.</span> </td>
		</tr>
	</table>
</div>
<script src="${pageContext.request.contextPath}/app-resources/js/lib/angular.min.js"></script>
<script src="${pageContext.request.contextPath}/app-resources/js/lib/angular-resource.min.js"></script>
<script src="${pageContext.request.contextPath}/app-resources/js/app.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/app-resources/css/style.css"/>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
</body>
</html>  
  