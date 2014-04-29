'use strict';

describe('Controller: SystemPermissionCtrl', function () {

  // load the controller's module
  beforeEach(module('yoApp'));

  var SystemPermissionCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    SystemPermissionCtrl = $controller('SystemPermissionCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
