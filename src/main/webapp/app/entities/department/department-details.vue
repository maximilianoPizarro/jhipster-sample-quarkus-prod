<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="department">
        <h2 class="jh-entity-heading" data-cy="departmentDetailsHeading">
          <span v-text="t$('jhipsterSampleQuarkusProdApp.department.detail.title')"></span> {{ department.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('jhipsterSampleQuarkusProdApp.department.departmentName')"></span>
          </dt>
          <dd>
            <span>{{ department.departmentName }}</span>
          </dd>
          <dt>
            <span v-text="t$('jhipsterSampleQuarkusProdApp.department.location')"></span>
          </dt>
          <dd>
            <div v-if="department.location">
              <router-link :to="{ name: 'LocationView', params: { locationId: department.location.id } }">{{
                department.location.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="department.id"
          :to="{ name: 'DepartmentEdit', params: { departmentId: department.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./department-details.component.ts"></script>
