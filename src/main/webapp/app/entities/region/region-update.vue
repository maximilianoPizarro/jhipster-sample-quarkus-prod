<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="jhipsterSampleQuarkusProdApp.region.home.createOrEditLabel"
          data-cy="RegionCreateUpdateHeading"
          v-text="t$('jhipsterSampleQuarkusProdApp.region.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="region.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="region.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('jhipsterSampleQuarkusProdApp.region.regionName')" for="region-regionName"></label>
            <input
              type="text"
              class="form-control"
              name="regionName"
              id="region-regionName"
              data-cy="regionName"
              :class="{ valid: !v$.regionName.$invalid, invalid: v$.regionName.$invalid }"
              v-model="v$.regionName.$model"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./region-update.component.ts"></script>
