'use strict';

describe('Controller: SystemRoleCtrl', function () {

  // load the controller's module
  beforeEach(module('yoApp'));

  var SystemRoleCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    SystemRoleCtrl = $controller('SystemRoleCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
