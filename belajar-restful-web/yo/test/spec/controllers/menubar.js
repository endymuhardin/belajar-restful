'use strict';

describe('Controller: MenubarCtrl', function () {

  // load the controller's module
  beforeEach(module('yoApp'));

  var MenubarCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    MenubarCtrl = $controller('MenubarCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
