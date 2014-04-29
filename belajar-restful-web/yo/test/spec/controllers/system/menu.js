'use strict';

describe('Controller: SystemMenuCtrl', function () {

  // load the controller's module
  beforeEach(module('yoApp'));

  var SystemMenuCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    SystemMenuCtrl = $controller('SystemMenuCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
