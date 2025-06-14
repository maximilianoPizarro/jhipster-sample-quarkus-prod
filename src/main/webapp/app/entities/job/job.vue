<template>
  <div>
    <h2 id="page-heading" data-cy="JobHeading">
      <span v-text="t$('jhipsterSampleQuarkusProdApp.job.home.title')" id="job-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('jhipsterSampleQuarkusProdApp.job.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'JobCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-job">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('jhipsterSampleQuarkusProdApp.job.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && jobs && jobs.length === 0">
      <span v-text="t$('jhipsterSampleQuarkusProdApp.job.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="jobs && jobs.length > 0">
      <table class="table table-striped" aria-describedby="jobs">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('jobTitle')">
              <span v-text="t$('jhipsterSampleQuarkusProdApp.job.jobTitle')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'jobTitle'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('minSalary')">
              <span v-text="t$('jhipsterSampleQuarkusProdApp.job.minSalary')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'minSalary'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('maxSalary')">
              <span v-text="t$('jhipsterSampleQuarkusProdApp.job.maxSalary')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'maxSalary'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('employee.id')">
              <span v-text="t$('jhipsterSampleQuarkusProdApp.job.employee')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'employee.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="job in jobs" :key="job.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'JobView', params: { jobId: job.id } }">{{ job.id }}</router-link>
            </td>
            <td>{{ job.jobTitle }}</td>
            <td>{{ job.minSalary }}</td>
            <td>{{ job.maxSalary }}</td>
            <td>
              <div v-if="job.employee">
                <router-link :to="{ name: 'EmployeeView', params: { employeeId: job.employee.id } }">{{ job.employee.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'JobView', params: { jobId: job.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'JobEdit', params: { jobId: job.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(job)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="jhipsterSampleQuarkusProdApp.job.delete.question"
          data-cy="jobDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-job-heading" v-text="t$('jhipsterSampleQuarkusProdApp.job.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-job"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeJob()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="jobs && jobs.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./job.component.ts"></script>
