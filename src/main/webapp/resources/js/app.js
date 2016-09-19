var app = angular.module('myApp', ['ngResource']);

app.factory('Company', ['$resource', function ($resource) {
    return $resource('http://localhost:8090/company/:companyId/', {companyId: '@id'},
	{
		updateCompany: {method: 'PUT'}
	}
    );
}]);

app.controller('CompanyController', ['$scope', 'Company', function($scope, Company) {
	var ob = this;
	ob.companies=[];
	ob.company = new Company();
	ob.fetchAllCompanies = function(){
		ob.companies = Company.query();
	};

	ob.fetchAllCompanies();

	ob.addCompany = function(){
		console.log('Inside save');
		if($scope.companyForm.$valid) {
			ob.company.$save(function(company){
					console.log(company);
					ob.flag= 'created';
					ob.reset();
					ob.fetchAllCompanies();
				},
				function(err){
					console.log(err.status);
					ob.flag='failed';
				}
			);
		}
	};

	ob.editCompany = function(id){
		console.log('Inside edit');
		ob.company = Company.get({ companyId: id}, function() {
			ob.flag = 'edit';
		});
	};

	ob.updateCompanyDetail = function(){
		console.log('Inside update');
		if($scope.companyForm.$valid) {
			ob.company.$updateCompany(function(company){
				console.log(company);
				ob.updatedId = company.id;
				ob.reset();
				ob.flag = 'updated';
				ob.fetchAllCompanies();
			});
		}
	};

	ob.deleteCompany = function(id){
		console.log('Inside delete');
		ob.company = Company.delete({ companyId: id}, function() {
			ob.reset();
			ob.flag = 'deleted';
			ob.fetchAllCompanies();
		});
	};

	ob.reset = function(){
		ob.company = new Company();
		$scope.companyForm.$setPristine();
	};

	ob.cancelUpdate = function(id){
		ob.company = new Company();
		ob.flag= '';
		ob.fetchAllCompanies();
	};
}]);    
   