const URL = 'http://localhost:8081/api/v1/users';

const usersList = document.querySelector('#users-list');
const userModal = document.querySelector('#user-modal')

function dataFromForm(formData) {
    const data = {};
    for (let field of formData) {
        const {name} = field;
        if (name) {
            const {type, value} = field;
            if (name === 'roles') {
                data[name] = [value];
            } else if (type === 'text' || type === 'email' || type === 'password') {
                data[name] = value;
            } else if (type === 'number') {
                data[name] = +value;
            }
        }
    }
    return data;
}

function getAllUsers() {
    fetch(URL)
        .then(res => res.json())
        .then(data => {
            renderAllUsers(data);
        });
}

function getModalEdit(id) {
    console.log('edit button');
    fetch(URL + '/' + id)
        .then(res => res.json())
        .then(data => renderModalEdit(data));
}

function getModalDelete(id) {
    console.log(id);
    fetch(URL + '/' + id)
        .then(res => res.json())
        .then(data => renderModalDelete(data));
}

const renderAllUsers = (users) => {
    let output = '';
    users.forEach(user => {
        let userRoles = ' '
        user.roles.forEach(role => userRoles += role.role.split('_')[1] + ' ')
        output += `
            <tr>
                <td>${user.id}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${userRoles}<td>
                <td>
                    <button type="button" class="btn btn-info edit-btn" data-toggle="modal"
                    onclick="getModalEdit(${user.id})">Edit</button>
                </td>
                <td>
                    <button type="button" class="btn btn-danger" data-toggle="modal"
                    onclick="getModalDelete(${user.id})">Delete</button>
                </td>
            </tr>
        `;
    });
    usersList.innerHTML = output;
}

const renderModalEdit = (user) => {
    userModal.innerHTML = `
        <div class="modal fade" id="edit" role="dialog" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit user</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="row justify-content-center align-items-center">
                            <form class="text-center" id="form-user-edit">
                                <div class="form-group font-weight-bold">
                                    <label>Id</label>
                                    <input type="number" class="form-control" name="id" value="${user.id}" readonly>
                                    <label>FirstName</label>
                                    <input type="text" class="form-control" name="firstName" value="${user.firstName}">
                                    <label>LastName</label>
                                    <input type="text" class="form-control" name="lastName" value="${user.lastName}">
                                    <label>Age</label>
                                    <input type="text" class="form-control" name="age" value="${user.age}">
                                    <label>Email</label>
                                    <input type="email" class="form-control" name="email" value="${user.email}">
                                    <label>Password</label>
                                    <input type="password" class="form-control" name="password" value="">
                                    <label>Role</label>
                                    <select multiple class="form-select w-100" size="2" id="role" name="roles">
                                        <option value="ROLE_ADMIN">ADMIN</option>
                                        <option value="ROLE_USER">USER</option>
                                    </select>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-danger" onclick="userEdit()">Edit</button>
                    </div>
                </div>
            </div>
        </div>
    `;
    $("#edit").modal();
}

function userEdit() {
    const formUserEdit = document.forms.namedItem('form-user-edit');
    const data = dataFromForm(formUserEdit);
    console.log(data);
    fetch(URL, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(() => {
        getAllUsers();
        $('#edit').modal('hide');
    });
}

const renderModalDelete = (user) => {
    console.log('This is renderModalDelete')
    userModal.innerHTML = `
        <div class="modal fade" id="delete" role="dialog" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Delete user</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="row justify-content-center align-items-center">
                            <form id="form-user-delete" class="text-center">
                                <div class="form-group font-weight-bold">
                                    <label>Id</label>
                                    <input type="number" class="form-control" value="${user.id}" readonly>
                                    <label>FirstName</label>
                                    <input type="text" class="form-control" value="${user.firstName}" readonly>
                                    <label>LastName</label>
                                    <input type="text" class="form-control" value="${user.lastName}" readonly>
                                    <label>Age</label>
                                    <input type="text" class="form-control" value="${user.age}" readonly>
                                    <label>Email</label>
                                    <input type="email" class="form-control" value="${user.email}" readonly>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-danger" onclick="userDelete(${user.id})">Delete</button>
                    </div>
                </div>
            </div>
        </div>
    `;
    $("#delete").modal();
}

function userDelete(id) {
    console.log('Delete User is: ' + id)
    fetch(URL + '/' + id, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(() => {
        getAllUsers();
        $('#delete').modal('hide');
    });
}